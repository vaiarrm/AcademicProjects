#######################################################################################################################
# FILE: Convolutional.py
# AUTHOR : Vaibhav Sharma
# DATE: 11/08/2016
# DESCRIPTION: Describes Convolutional Layer
#######################################################################################################################

# External Libraries

import numpy as np
from collections import deque

# Internal Libraries

import Activations


class convolutional(object):

    def __init__(self, inputDimension, localReceptiveFieldDim, padding, stride, poolingDim, outDepth, outDim,
                 activation=Activations.Relu()):

        self.__inputDimension = inputDimension
        self.__localReceptiveFieldDim = localReceptiveFieldDim
        self.__padding = padding
        self.__stride = stride
        self.__poolingDim = poolingDim
        self.__outDepth = outDepth
        self.__outDim = outDim
        self.__activation = activation
        self.__inputData = None

        self.__weights = deque()
        self.__biases = deque()
        for i in range(self.__outDepth):
            w = np.random.randn(self.__localReceptiveFieldDim, self.__localReceptiveFieldDim, self.__inputDimension[-1])\
                / np.sqrt(self.__localReceptiveFieldDim)
            b = np.random.randn() / np.sqrt(self.__localReceptiveFieldDim)
            self.__weights.append(w)
            self.__biases.append(b)

        self.__convZOutput = np.zeros((self.__calculateConvOutputDim(), self.__calculateConvOutputDim(), self.__outDepth))
        self.__convAOutput = np.zeros((self.__calculateConvOutputDim(), self.__calculateConvOutputDim(), self.__outDepth))
        self.__poolOutput = np.zeros((self.__calculatePoolOutputDim(), self.__calculatePoolOutputDim(), self.__outDepth))

    def maxPooling(self):
        """
            Pooling layer
        :return: Output of pooling layer
        """
        for i in xrange(self.__outDepth):
            layer = self.__convAOutput[:,:,i]
            for j in xrange(0, self.__poolOutput.shape[0]):
                for k in xrange(0, self.__poolOutput.shape[1]):
                    poolX = j * self.__poolingDim
                    poolY = k * self.__poolingDim
                    arrSec = layer[poolX:poolX+self.__poolingDim, poolY:poolY+self.__poolingDim]
                    self.__poolOutput[j][k][i] = np.max(arrSec)
        return self.__poolOutput

    def convolve(self, data):
        """
            Convolutional layer
        :param data:
        :return: Ouput of convoluitonal layer
        """
        self.__inputData = data
        npad = ((self.__padding, self.__padding), (self.__padding, self.__padding), (0, 0))
        data = np.pad(data, pad_width=npad, mode='constant', constant_values=0)
        for i in xrange(self.__outDepth):
            w = self.__weights[i]
            b = self.__biases[i]
            for j in xrange(0, self.__convAOutput.shape[0],self.__stride):
                for k in xrange(0, self.__convAOutput.shape[1],self.__stride):
                    arrSec = data[j:j+self.__localReceptiveFieldDim, k:k+self.__localReceptiveFieldDim, :]
                    z = w * arrSec + b
                    self.__convZOutput[j][k][i] = np.sum(z)
                    self.__convAOutput[j][k][i] = self.__activation.actFunction(self.__convZOutput[j][k][i])
        del data
        return self.__convAOutput

    def backpropogate(self, deltas, prevWeight, eta, lmbda, n, isLastConvLayer=False):
        """
            Backpropogation layer
        :param deltas:
        :param prevWeight:
        :param eta:
        :param lmbda:
        :param n:
        :param isLastConvLayer:
        :return:
        """
        dShape = deltas.shape
        deltas = self.__unpoolDelta(deltas)

        if isLastConvLayer:
            temp = np.sum(prevWeight, 1).reshape((prevWeight.shape[0],1))
            temp = temp / prevWeight.shape[0]
            temp = temp.reshape(dShape)
            wt = self.__unpoolDelta(temp)
            wRot180 = np.rot90(wt, 2)
            del temp
            deltaCurr = np.einsum('nmk,nmk->nmk', deltas, wRot180)
            deltaCurr *= self.__activation.actFunPrime(self.__convZOutput)
            wtGrad = np.einsum('nmk,nmk->nmk', deltaCurr, np.rot90(self.__inputData, 2))

            #updating biases

            for i in xrange(deltaCurr.shape[2]):
                layer = deltaCurr[:, :, i]
                dChange = np.sum(layer)/np.size(layer)
                self.__biases[i] -= eta * dChange

            # updating weights
            wtShape = self.__weights[0].shape
            for i in range(wtShape[2]):
                for j in range(wtShape[0]):
                    for k in range(wtShape[1]):
                        dist = wtGrad.shape[0] - self.__localReceptiveFieldDim + 1
                        sec = wtGrad[j:j+dist, k:k+dist, i ]
                        wtChange = eta * np.sum(sec) / np.size(sec)
                        w = self.__weights[i][j][k]
                        w *= (1 - eta * (lmbda / n))
                        self.__weights[i][j][k] = w - wtChange

            return deltaCurr, self.__weights
        else:
            npad = ((self.__padding, self.__padding), (self.__padding, self.__padding), (0, 0))
            deltas = np.pad(deltas, pad_width=npad, mode='constant', constant_values=0)
            currDelta = np.zeros((self.__calculateConvOutputDim(), self.__calculateConvOutputDim(), self.__outDepth))
            for i in xrange(self.__outDepth):
                w = prevWeight[i]
                wRot180 = np.rot90(w, 2)
                for j in xrange(0, self.__convAOutput.shape[0], self.__stride):
                    for k in xrange(0, self.__convAOutput.shape[1], self.__stride):
                        arrSec = deltas[j:j+self.__localReceptiveFieldDim, k:k+self.__localReceptiveFieldDim, :]
                        C = np.einsum('nmk,nmk->nmk', arrSec, wRot180)
                        currDelta[j][k][i] = np.sum(C)/np.size(C)
            prime = self.__activation.actFunPrime(self.__convZOutput)
            currDelta = currDelta * prime

            del prime
            del deltas

            prevActivation180 = np.rot90(self.__inputData, 2)
            wtGrad = np.einsum('nmk,nmk->nmk', currDelta, prevActivation180)

            # updating biases

            for i in xrange(currDelta.shape[2]):
                layer = currDelta[:, :, i]
                dChange = np.sum(layer) / np.size(layer)
                self.__biases[i] -= eta * dChange

            # updating weights
            wtShape = self.__weights[0].shape
            for i in range(wtShape[2]):
                for j in range(wtShape[0]):
                    for k in range(wtShape[1]):
                        dist = wtGrad.shape[0] - self.__localReceptiveFieldDim + 1
                        sec = wtGrad[j:j + dist, k:k + dist, i]
                        wtChange = eta * np.sum(sec) / np.size(sec)
                        w = self.__weights[i][j][k]
                        w *= (1 - eta * (lmbda / n))
                        self.__weights[i][j][k] = w - wtChange
            return currDelta, self.__weights

    def getZVals(self):
        """
        Returns Z values
        :return: Z values
        """
        return self.__convZOutput

    def getAVals(self):
        """
        Returns Activation Values
        :return: Activation values
        """
        return self.__convAOutput

    # Private Methods

    def __unpoolDelta(self, deltas):
        """
            Reverse pooling operation while backpropogation
        :param deltas:
        :return:
        """
        temp = np.zeros(self.__convAOutput.shape)
        for i in xrange(self.__outDepth):
            for j in xrange(0, self.__poolOutput.shape[0]):
                for k in xrange(0, self.__poolOutput.shape[1]):
                    poolX = j * self.__poolingDim
                    poolY = k * self.__poolingDim
                    for a in range(poolX,poolX+self.__poolingDim):
                        for b in range(poolY, poolY + self.__poolingDim):
                            temp[a][b][i] = deltas[j][k][i] / self.__poolingDim
        return temp


    def __calculateConvOutputDim(self):
        """
            Calculates output dimesnions
        :return: Ouput dimnesion
        """
        W = self.__inputDimension[0]
        F = self.__localReceptiveFieldDim
        P = self.__padding
        S = self.__stride

        return (W - F + 2 * P)/S + 1

    def __calculatePoolOutputDim(self):
        """
            Calculates pooling dimension
        :return: Pooling Dimension
        """
        s = self.__convAOutput.shape
        return s[0] / self.__poolingDim
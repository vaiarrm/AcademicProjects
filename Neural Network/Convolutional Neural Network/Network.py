#######################################################################################################################
# FILE: Network.py
# AUTHOR : Vaibhav Sharma
# DATE: 11/18/2016
# DESCRIPTION: Describes the complete Network
#######################################################################################################################

# External Libraries
from __future__ import division
#from tqdm import tqdm
import numpy as np
import random
import matplotlib.pyplot as plt

# Application Libraries
import Activations
import CostFunction
from Convolutional import convolutional
from FullConnected import fullconnected


class Network(object):

    def __init__(self, layers, dataObject, numEpochs, eta, lmbda):
        """
        Creates the network
        :param layers: Details about convolutional and full connected layers
        :param dataObject: Data object from DataLoader.py
        :param numEpochs: Number of times to train the network
        :param eta: Learning rate
        :param lmbda: For L2-Regularization
        """
        # Initializing from input parameters
        self.__layers = layers
        self.__dataObj = dataObject
        self.__data = self.__dataObj.readFile()
        self.__numEpochs = numEpochs
        self.__eta = eta
        self.__lmbda = lmbda

        # Initializing list for accuracies
        self.__train_acc_list = list()
        self.__train_err_list = list()
        self.__valid_acc_list = list()
        self.__valid_err_list = list()
        self.__test_acc_list = list()
        self.__test_err_list = list()

        # creating Convolutional layer
        conv = self.__layers[0]
        self.__convLayer = []
        fullLayersDim = []
        lastConvOut = None
        for item in conv:
            c = convolutional(item[0], item[1], item[2], item[3], item[4], item[5], item[6])
            self.__convLayer.append(c)
            lastConvOut = item[6]

        # creating fully connected layer

        full = self.__layers[1]
        exFullDim = lastConvOut[0] * lastConvOut[1] * lastConvOut[2]
        fullLayersDim.append(exFullDim)  # input to Fully connected layer
        fullLayersDim.append(exFullDim)  # extra hidden layer defined
        for item in full:
            fullLayersDim.append(item)
        fullLayersDim.append(len(self.__data[0][0][1]))
        activation = Activations.Sigmoid()
        lastActivation = Activations.Softmax()
        lossFun = CostFunction.Log
        self.__fullLayers = fullconnected(fullLayersDim, activation, lastActivation, lossFun)

    def run(self):
        """
        Train, validates and tests the network
        :return: None
        """

        for i in xrange(self.__numEpochs):

            size = 40
            # Training

            train_data = self.__data[0]
            a = random.randint(0, len(train_data)-size)

            train_data = train_data[a:a+size]
            train_error = 0
            train_correct = 0

            for item in train_data:
                train_input = item[0]
                expected_output = item[1]

                for conv in self.__convLayer:
                    conv.convolve(train_input)
                    train_input = conv.maxPooling()

                tis = train_input.shape
                t = tis[0] * tis[1] * tis[2]
                train_input=train_input.reshape((t, 1))

                fullLayerInput = train_input,expected_output

                error, correct, delta = self.__fullLayers.SGD(fullLayerInput, len(train_data), self.__eta, self.__lmbda)

                train_error += error
                if correct:
                    train_correct += 1


                delta = delta.reshape(tis)
                c = self.__convLayer[-1]

                delta, preWt = c.backpropogate(delta, self.__fullLayers.getFirstWeight(), self.__eta, self.__lmbda, len(train_data), True)
                for k in range(-1, -1 * len(self.__convLayer) - 1, -1):
                    c = self.__convLayer[k]
                    delta, preWt = c.backpropogate(delta, preWt, self.__eta, self.__lmbda, len(train_data), False)


            self.__train_acc_list.append(train_correct / len(train_data))
            self.__train_err_list.append(train_error / len(train_data))

            del train_data

            # validation

            valid_data = self.__data[1]
            a = random.randint(0, len(valid_data) - size//2)
            valid_data = valid_data[a:a + size//2]
            valid_error = 0
            valid_correct = 0

            for item in valid_data:
                valid_input = item[0]
                valid_output = item[1]

                for conv in self.__convLayer:
                    conv.convolve(valid_input)
                    valid_input = conv.maxPooling()

                tis = valid_input.shape
                t = tis[0] * tis[1] * tis[2]
                valid_input= valid_input.reshape((t, 1))

                fullLayerInput = valid_input, valid_output

                error, correct= self.__fullLayers.feedforward(fullLayerInput)

                valid_error += error
                if correct:
                    valid_correct += 1

            self.__valid_acc_list.append(valid_correct / len(valid_data))
            self.__valid_err_list.append(valid_error / len(valid_data))

            del valid_data

            # testing

            test_data = self.__data[2]
            a = random.randint(0, len(test_data) - size//2)
            test_data = test_data[a:a + size//2]
            test_error = 0
            test_correct = 0

            for item in test_data:
                test_input = item[0]
                test_output = item[1]

                for conv in self.__convLayer:
                    conv.convolve(test_input)
                    test_input = conv.maxPooling()

                tis = test_input.shape
                t = tis[0] * tis[1] * tis[2]
                test_input=test_input.reshape((t, 1))

                fullLayerInput = test_input, test_output

                error, correct= self.__fullLayers.feedforward(fullLayerInput)

                test_error += error
                if correct:
                    test_correct += 1

            self.__test_acc_list.append(test_correct / len(test_data))
            self.__test_err_list.append(test_error / len(test_data))

            del test_data
            print "For Epoch: {} ".format(i+1)

            print "\tCost: Training: {:.2f} Validation: {:.2f} Test: {:.2f}".format(self.__train_err_list[i],
                                                                      self.__valid_err_list[i],
                                                                      self.__test_err_list[i])

            print "\tAccuracies: Training: {:.2f} Validation: {:.2f} Test: {:.2f}".format(self.__train_acc_list[i],
                                                                            self.__valid_acc_list[i],
                                                                            self.__test_acc_list[i])

        x = np.arange(len(self.__train_acc_list))
        plt.figure(1)
        plt.subplot(211)
        plt.title(self.__dataObj.getDataName())
        plt.ylabel("Cost")
        plt.plot(x, self.__train_err_list, color="r", label="Training Cost")
        plt.plot(x, self.__valid_err_list, color="g", label="Validation Cost")
        plt.plot(x, self.__test_err_list, color="b", label="Testing Cost")
        plt.legend(loc='best')

        plt.subplot(212)
        plt.ylabel("Accuracies")
        plt.plot(x, self.__train_acc_list, color="r", label="Training Accuracy")
        plt.plot(x, self.__valid_acc_list, color="g", label="Validation Accuracy")
        plt.plot(x, self.__test_acc_list, color="b", label="Testing Accuracy")
        plt.legend(loc='best')
        plt.show()



        return self.__train_acc_list, self.__valid_acc_list, self.__test_acc_list, self.__train_err_list,\
               self.__valid_err_list, self.__test_err_list





    def speedTestFeedForward2(self):
        """
            Testing for Efficinecy
        :return:
        """
        test_data = self.__data[2][:500]
        fin = None
        fout = None
        first = True
        for item in test_data:
            test_input = item[0]
            test_output = item[1]
            if not first:
                fout = np.hstack((fout, test_output))
            else:
                fout = test_output
            for conv in self.__convLayer:
                conv.convolve(test_input)
                test_input = conv.maxPooling()

            tis = test_input.shape
            t = tis[0] * tis[1] * tis[2]
            test_input = test_input.reshape((t, 1))
            if not first:
                fin = np.hstack((fin, test_input))
            else:
                fin = test_input
            first = False
        self.__fullLayers.feedforward2((fin,fout))

    def speedTestFeedForward(self):
        """
            Testing for efficiency
        :return:
        """
        test_data = self.__data[2][:500]
        for item in test_data:
            test_input = item[0]
            test_output = item[1]

            for conv in self.__convLayer:
                conv.convolve(test_input)
                test_input = conv.maxPooling()

            tis = test_input.shape
            t = tis[0] * tis[1] * tis[2]
            test_input = test_input.reshape((t, 1))
            fullLayerInput = test_input, test_output
            self.__fullLayers.feedforward(fullLayerInput)

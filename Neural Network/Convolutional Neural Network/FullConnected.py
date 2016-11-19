#######################################################################################################################
# FILE: FullyConnected.py
# AUTHOR : Vaibhav Sharma
# DATE: 11/13/2016
# DESCRIPTION: Describes Full Connected Layer
#######################################################################################################################

import numpy as np


class fullconnected(object):
    """
    Fully Connected Layer
    """

    def __init__(self, layers, activation, lastActivation, lossFun):
        """
        Created Fully Connected Layers
        :param layers: List of layers
        :param activation: Activation function for layers other than last layer
        :param lastActivation: Activation function for last layer
        :param lossFun: Loss function
        """
        self.__num_layers = len(layers)
        self.__layers = layers
        self.__activation = activation
        self.__lastActivation = lastActivation
        self.__lossFun = lossFun

        self.__weights = [np.random.randn(y, x)/np.sqrt(x) for x, y in zip(self.__layers[:-1], self.__layers[1:])]
        self.__biases = [np.random.randn(y, 1)//np.sqrt(y) for y in self.__layers[1:]]

    def SGD(self, data, n, eta=0.1, lmbda=0.0):
        """
        Stochastic Gradient Descent
        :param data:
        :param n: Total size of data set
        :param eta: learning rate
        :param lmbda: L2-Regularization
        :return: error, correct and delta
        """
        x = data[0]
        y = data[1]
        nabla_b, nabla_w, error, correct, delta = self.backprop(x, y)

        self.__weights = [(1 - eta * (lmbda / n)) * w - eta * nw for w, nw in zip(self.__weights, nabla_w)]
        self.__biases = [b - eta * nb for b, nb in zip(self.__biases, nabla_b)]

        return error, correct, delta

    def backprop(self, x, y):
        """
        BackPropagation in Fully Connected Layer
        :param x: Input data
        :param y: Expected Output
        :return: Gradients, error, correct and delta
        """

        nabla_b = [np.zeros(b.shape) for b in self.__biases]
        nabla_w = [np.zeros(w.shape) for w in self.__weights]

        activation = x
        activations = [x]
        zs = []
        i = 0
        lenW = len(self.__weights) - 1
        act = None
        for b, w in zip(self.__biases, self.__weights):
            if i == lenW:
                act = self.__lastActivation
            else:
                act = self.__activation
            i += 1
            z = np.dot(w, activation) + b
            zs.append(z)
            activation = act.actFunction(z)
            activations.append(activation)

        # backward pass
        error = self.__lossFun.costFunction(activations[-1], y)
        correct = act.correctCalc(activations[-1], y)
        delta = self.__lossFun.costDelta(zs[-1], activations[-1], y)
        nabla_b[-1] = delta
        nabla_w[-1] = np.dot(delta, activations[-2].transpose())

        for l in xrange(2, self.__num_layers):
            z = zs[-l]
            sp = self.__activation.actFunPrime(z)
            delta = np.dot(self.__weights[-l + 1].transpose(), delta) * sp
            nabla_b[-l] = delta
            nabla_w[-l] = np.dot(delta, activations[-l - 1].transpose())

        return nabla_b, nabla_w, error, correct, delta

    def feedforward(self, data):
        """
        Feedforward for validation and testing
        :param data:
        :return: error and boolean for correctness
        """
        x = data[0]
        y = data[1]
        activation = x
        i = 0
        lenW = len(self.__weights) - 1
        act = None
        for b, w in zip(self.__biases, self.__weights):
            if i == lenW:
                act = self.__lastActivation
            else:
                act = self.__activation
            i += 1
            z = np.dot(w, activation) + b
            activation = act.actFunction(z)

        error = self.__lossFun.costFunction(activation, y)
        correct = act.correctCalc(activation, y)

        return error, correct

    def getFirstWeight(self):
        return self.__weights[0]

    def feedforward2(self, data):
        """
        Feedforward for validation and testing
        :param data:
        :return: error and boolean for correctness
        """
        x = data[0]
        y = data[1]
        activation = x
        i = 0
        lenW = len(self.__weights) - 1
        act = None
        for b, w in zip(self.__biases, self.__weights):
            if i == lenW:
                act = self.__lastActivation
            else:
                act = self.__activation
            i += 1
            z = np.dot(w, activation) + np.repeat(b, x.shape[1], 1)
            activation = act.actFunction(z)
        error = self.__lossFun.costFunction(activation,y)
        error = np.sum(error)
        correct = 0
        for k in range(activation.shape[1]):
            a = activation[:, k]
            yd = y[:, k]
            if act.correctCalc(a, yd):
                correct += 1
        return error, correct
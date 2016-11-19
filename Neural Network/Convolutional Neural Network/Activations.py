#######################################################################################################################
# FILE: Activations.py
# AUTHOR : Vaibhav Sharma
# DATE: 11/13/2016
# DESCRIPTION: Lists the activation functions
#######################################################################################################################

# External Libraries

import numpy as np

class Activation(object):
    """
        Interface for all the activation functions
    """
    def actFunction(self, z):
        """
        Calculates actvation values
        :param z:
        :return: Activation Values
        """
        raise NotImplementedError("This is an interface for all the activation functions")

    def actFunPrime(self, z):
        """
        Calculates prime of activation function
        :param z:
        :return: Derivative of activation funciton
        """
        raise NotImplementedError("This is an interface for all the activation functions")

    def correctCalc(self, a, y):
        """
        Tests if activation is same as expected output
        See Softmax for its implementation
        :param a:
        :param y:
        :return: True if the calculated activation matches output
        """
        x = np.round(a)
        return x.all() == y.all()


class Sigmoid(Activation):
    """
    Sigmoid Activation Function
    """

    def actFunction(self, z):
        return 1.0 / (1.0 + np.exp(-z))

    def actFunPrime(self, z):
        return self.actFunction(z) * (1 - self.actFunction(z))

class Relu(Activation):
    """
    Relu Activation Function
    """

    def actFunction(self, z):
        return np.maximum(0, z)

    def actFunPrime(self, z):
        return 1.0 * (z > 0)


class Softmax(Activation):
    """
    Softmax Activation Function
    """

    def actFunction(self, z):
        e_x = np.exp(z - np.max(z))
        return e_x / e_x.sum()

    def actFunPrime(self, z):
        return self.actFunction(z) * (1 - self.actFunction(z))

    def correctCalc(self, a, y):

        index = np.argmax(a)
        x = np.zeros(y.shape)
        x[index] = 1.0
        k = x == y
        return k.all()


class Tanh(Activation):
    """
    tanh Activation Function
    """

    def actFunction(self, z):
        return np.tanh(z)

    def actFunPrime(self, z):
        return 1 - np.power(self.actFunction(z), 2)
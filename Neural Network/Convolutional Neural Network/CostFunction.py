#######################################################################################################################
# FILE: CostFunction.py
# AUTHOR : Vaibhav Sharma
# DATE: 11/13/2016
# DESCRIPTION: Describes Cost Functions
#######################################################################################################################

# External Libraties
import numpy as np


class Cost(object):

    @staticmethod
    def costFunction(a,y):
        raise NotImplementedError("This is an interface for different cost functions")

    @staticmethod
    def costDelta(z, a, y, activation=None):
        raise NotImplementedError("This is an interface for different cost functions")


class CrossEntropyCost(Cost):
    """
        Cross Entropy Cost Function
    """
    @staticmethod
    def costFunction(a, y):
        return np.sum((-y * np.nan_to_num(np.log(a))) - ((1-y) * np.nan_to_num(np.log(a))))

    @staticmethod
    def costDelta(z, a, y, activation=None):
        return a-y


class QuadraticCost(Cost):
    """
        Quadratic Cost Function
    """
    @staticmethod
    def costFunction(a, y):
        return 0.5*np.linalg.norm(a-y)**2

    @staticmethod
    def costDelta(z, a, y, activation=None):
        return (a-y) * activation.actFunPrime(z)

class Log(Cost):
    """
        Log Cost Function
    """

    @staticmethod
    def costFunction(a, y):
        index = np.argmax(y)
        return -1 * np.sum(np.log(a[index]))

    @staticmethod
    def costDelta(z, a, y, activation=None):
        return a - y
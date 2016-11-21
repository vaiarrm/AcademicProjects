#######################################################################################################################
# FILE: DataLoader.py
# AUTHOR : Vaibhav Sharma
# DATE: 11/08/2016
# DESCRIPTION: Loads Different Types of MNIST and CIFAR-10 files
#######################################################################################################################

# External Libraries

import cPickle
import gzip
import numpy as np
import os


class Data(object):
    """
        Interface for basic data set loaders
    """
    def readFile(self):
        raise NotImplementedError("This is an interface for MNIST and CIFAR-1XX data sets")

    def getDataInfo(self):
        raise NotImplementedError("This is an interface for MNIST and CIFAR-1XX data sets")

    def getDataName(self):
        raise NotImplementedError("This is an interface for MNIST and CIFAR-1XX data sets")


class MnistDataLoader(Data):
    """
        Loads Basic MNIST Data
    """
    def __init__(self):
        self.__dataName = "MNIST Basic"

    def getDataName(self):
        """
        :return: Name of Data Set
        """
        return self.__dataName

    def readFile(self):
        path = os.path.join(os.getcwd(),"data","mnist.pkl.gz") #'data/mnist.pkl.gz'
        f = gzip.open(path, 'rb')
        training_data, validation_data, test_data = cPickle.load(f)
        return self.__processData(training_data,validation_data,test_data)
        f.close()

    def getDataInfo(self):
        return [28,28,1]

    def __processData(self,training_data, validation_data, test_data ):
        td_input = [np.reshape(x, (28,28,1)) for x in training_data[0]]
        td_result = [self.__vectorizedResult(y) for y in training_data[1]]
        training_data = zip(td_input, td_result)

        v_input = [np.reshape(x, (28, 28, 1)) for x in validation_data[0]]
        v_result = [self.__vectorizedResult(y) for y in validation_data[1]]
        validation_data = zip(v_input, v_result)

        test_input = [np.reshape(x, (28, 28, 1)) for x in test_data[0]]
        test_result = [self.__vectorizedResult(y) for y in test_data[1]]
        test_data = zip(test_input, test_result)

        return training_data,validation_data,test_data

    def __vectorizedResult(self,target):
        res = np.zeros((10,1))
        res[target] = 1
        return res


class MnistRandomBackground(Data):
    """
        Loads MNIST data with Random background
    """

    def __init__(self):
        self.__dataName = "MNIST Random Background"

    def getDataName(self):
        """
        :return: Name of Data Set
        """
        return self.__dataName

    def readFile(self):
        # path = os.getcwd() + '/data/mnist_background_random'
        # trainFilePath = path + "/mnist_background_random_train.amat"
        # testFilePath = path + "/mnist_background_random_test.amat"

        trainFilePath = os.path.join(os.getcwd(), "data", "mnist_background_random","mnist_background_random_train.amat")
        testFilePath = os.path.join(os.getcwd(), "data", "mnist_background_random", "mnist_background_random_test.amat")

        train = np.loadtxt(trainFilePath)
        numRows = len(train)
        numCols = len(train[0])
        trainData = train[:, :numCols - 1]
        trainData -= trainData.mean()
        trainData /= trainData.std()
        trainTarget = train[:, numCols - 1]
        trainTarget = trainTarget.reshape(numRows, 1)

        test = np.loadtxt(testFilePath)
        numRows = len(test)
        numCols = len(test[0])
        testData = test[:, :numCols - 1]
        testData -= testData.mean()
        testData /= testData.std()
        testTarget = test[:, numCols - 1]
        testTarget = testTarget.reshape(numRows, 1)

        return self.__processData(trainData, trainTarget, testData, testTarget)

    def getDataInfo(self):
        return [28, 28, 1]

    def __vectorizedResult(self, target):
        res = np.zeros((10, 1))
        res[target[0]] = 1
        return res

    def __processData(self, training_data,train_target, test_data,test_target):
        a = len(training_data) - 1000
        validData = training_data[a:, :]
        validTarget = train_target[a:, :]

        training_data = training_data[:a, :]
        train_target = train_target [:a,:]

        td_input = [np.reshape(x, (28, 28, 1)) for x in training_data]
        td_result = [self.__vectorizedResult(y) for y in train_target]
        training_data = zip(td_input, td_result)

        v_input = [np.reshape(x, (28, 28, 1)) for x in validData]
        v_result = [self.__vectorizedResult(y) for y in validTarget]
        validation_data = zip(v_input, v_result)

        test_input = [np.reshape(x, (28, 28, 1)) for x in test_data]
        test_result = [self.__vectorizedResult(y) for y in test_target]
        test_data = zip(test_input, test_result)

        return training_data, validation_data, test_data


class MnistBackground(Data):
    """
        Loads MNIST data with background
    """

    def __init__(self):
        self.__dataName = "MNIST Background"

    def getDataName(self):
        """
        :return: Name of Data Set
        """
        return self.__dataName

    def readFile(self):
        # path = os.getcwd() + '/data/mnist_background_images'
        # trainFilePath = path + "/mnist_background_images_train.amat"
        # testFilePath = path + "/mnist_background_images_test.amat"

        trainFilePath = os.path.join(os.getcwd(), "data", "mnist_background_images","mnist_background_images_train.amat")
        testFilePath = os.path.join(os.getcwd(), "data", "mnist_background_images", "mnist_background_images_test.amat")

        train = np.loadtxt(trainFilePath)
        numRows = len(train)
        numCols = len(train[0])
        trainData = train[:, :numCols - 1]
        trainData -= trainData.mean()
        trainData /= trainData.std()
        trainTarget = train[:, numCols - 1]
        trainTarget = trainTarget.reshape(numRows, 1)

        test = np.loadtxt(testFilePath)
        numRows = len(test)
        numCols = len(test[0])
        testData = test[:, :numCols - 1]
        testData -= testData.mean()
        testData /= testData.std()
        testTarget = test[:, numCols - 1]
        testTarget = testTarget.reshape(numRows, 1)

        return self.__processData(trainData, trainTarget, testData, testTarget)

    def getDataInfo(self):
        return [28, 28, 1]

    def __vectorizedResult(self, target):
        res = np.zeros((10,1))
        res[target[0]] = 1
        return res

    def __processData(self, training_data,train_target, test_data,test_target):
        a = len(training_data) - 1000
        validData = training_data[a:, :]
        validTarget = train_target[a:, :]

        training_data = training_data[:a, :]
        train_target = train_target [:a,:]

        td_input = [np.reshape(x, (28, 28, 1)) for x in training_data]
        td_result = [self.__vectorizedResult(y) for y in train_target]
        training_data = zip(td_input, td_result)

        v_input = [np.reshape(x, (28, 28, 1)) for x in validData]
        v_result = [self.__vectorizedResult(y) for y in validTarget]
        validation_data = zip(v_input, v_result)

        test_input = [np.reshape(x, (28, 28, 1)) for x in test_data]
        test_result = [self.__vectorizedResult(y) for y in test_target]
        test_data = zip(test_input, test_result)

        return training_data, validation_data, test_data


class MnistRotated(Data):
    """
        Loads MNIST Rotated Data
    """

    def __init__(self):
        self.__dataName = "MNIST Rotated"

    def getDataName(self):
        """
        :return: Name of Data Set
        """
        return self.__dataName

    def readFile(self):
        # path = os.getcwd() + '/data/mnist_rotation_new'
        # trainFilePath = path + "/mnist_all_rotation_normalized_float_train_valid.amat"
        # testFilePath = path + "/mnist_all_rotation_normalized_float_test.amat"

        trainFilePath = os.path.join(os.getcwd(), "data", "mnist_rotation_new", "mnist_all_rotation_normalized_float_train_valid.amat")
        testFilePath = os.path.join(os.getcwd(), "data", "mnist_rotation_new", "mnist_all_rotation_normalized_float_test.amat")


        train = np.loadtxt(trainFilePath)
        numRows = len(train)
        numCols = len(train[0])
        trainData = train[:, :numCols - 1]
        trainData -= trainData.mean()
        trainData /= trainData.std()
        trainTarget = train[:, numCols - 1]
        trainTarget = trainTarget.reshape(numRows, 1)

        test = np.loadtxt(testFilePath)
        numRows = len(test)
        numCols = len(test[0])
        testData = test[:, :numCols - 1]
        testData -= testData.mean()
        testData /= testData.std()
        testTarget = test[:, numCols - 1]
        testTarget = testTarget.reshape(numRows, 1)

        return self.__processData(trainData, trainTarget, testData, testTarget)

    def getDataInfo(self):
        return [28, 28, 1]

    def __vectorizedResult(self, target):
        res = np.zeros((10,1))
        res[target[0]] = 1
        return res

    def __processData(self, training_data,train_target, test_data,test_target):
        a = len(training_data) - 1000
        validData = training_data[a:, :]
        validTarget = train_target[a:, :]

        training_data = training_data[:a, :]
        train_target = train_target [:a,:]

        td_input = [np.reshape(x, (28, 28, 1)) for x in training_data]
        td_result = [self.__vectorizedResult(y) for y in train_target]
        training_data = zip(td_input, td_result)

        v_input = [np.reshape(x, (28, 28, 1)) for x in validData]
        v_result = [self.__vectorizedResult(y) for y in validTarget]
        validation_data = zip(v_input, v_result)

        test_input = [np.reshape(x, (28, 28, 1)) for x in test_data]
        test_result = [self.__vectorizedResult(y) for y in test_target]
        test_data = zip(test_input, test_result)

        return training_data, validation_data, test_data

class Cifar10DataLoader3Dim(Data):
    """
        Loads Cifar 10 dataset
    """

    def __init__(self):
        self.__dataName = "CIFAR 10"

    def getDataName(self):
        """
        :return: Name of Data Set
        """
        return self.__dataName

    def readFile(self):
        dirPath = os.path.join(os.getcwd(), "data", "cifar10")
        fNames = os.listdir(dirPath)
        training_data = []
        validation_data = []
        test_data = []
        for f in fNames:
            absPath = os.path.join(dirPath, f)
            if f == 'data_batch_5': # validation data
                validation_data += self.__readFile(absPath)
            elif f == 'test_batch': # test data
                test_data += self.__readFile(absPath)
            elif 'data_batch' in f:
                training_data += self.__readFile(absPath)

        return training_data, validation_data, test_data

    def getDataInfo(self):
        return [32, 32, 3]

    def __readFile(self,fileName):
        print "loading ", fileName
        fo = open(fileName, 'rb')
        dict = cPickle.load(fo)
        fo.close()
        return self.__processData(dict)

    def __processData(self, dict):
        data = dict['data']
        labels = dict['labels']
        td_result = []
        td_data = []
        for i in xrange(len(labels)):
            td_result.append(self.__vectorizedResult(labels[i]))
            x = data[i]
            y = []
            for j in xrange(1024):
                y.append(x[j])
                y.append(x[j+1024])
                y.append(x[j+2048])

            y = np.array(y)
            y = np.reshape(y, (32, 32, 3))
            td_data.append(y)
        return zip(td_data,td_result)

    def __vectorizedResult(self, target):
        res = np.zeros((10,1))
        res[target] = 1
        return res

class Cifar10DataLoader(Data):
    """
        Loads Cifar 10 dataset
    """

    def __init__(self):
        self.__dataName = "CIFAR 10"

    def getDataName(self):
        """
        :return: Name of Data Set
        """
        return self.__dataName

    def readFile(self):
        dirPath = os.path.join(os.getcwd(), "data", "cifar10")
        fNames = os.listdir(dirPath)
        training_data = []
        validation_data = []
        test_data = []
        for f in fNames:
            absPath = os.path.join(dirPath, f)
            if f == 'data_batch_5': # validation data
                validation_data += self.__readFile(absPath)
            elif f == 'test_batch': # test data
                test_data += self.__readFile(absPath)
            elif 'data_batch' in f:
                training_data += self.__readFile(absPath)

        return training_data, validation_data, test_data

    def getDataInfo(self):
        return [32, 32, 1]

    def __readFile(self,fileName):
        print "loading ", fileName
        fo = open(fileName, 'rb')
        dict = cPickle.load(fo)
        fo.close()
        return self.__processData(dict)

    def __processData(self, dict):
        data = dict['data']
        labels = dict['labels']
        td_result = []
        td_data = []
        for i in xrange(len(labels)):
            td_result.append(self.__vectorizedResult(labels[i]))
            x = data[i]
            y = []
            for j in xrange(1024):
                y.append(x[j])
                y.append(x[j+1024])
                y.append(x[j+2048])

            y = np.array(y)
            y = np.reshape(y, (32, 32, 3))
            td_data.append(y)
        temp=self.__convertToOneDim(td_data)
        return zip(temp,td_result)

    def __convertToOneDim(self,data):
        tempLst = []
        for item in data:
            temp = np.zeros((32,32,1))
            for i in xrange(32):
                for j in xrange(32):
                    temp[i][j][0] = (item[i][j][0] + item[i][j][1] + item[i][j][2]) / 3
                    tempLst.append(temp)
        return tempLst

    def __vectorizedResult(self, target):
        res = np.zeros((10,1))
        res[target] = 1
        return res

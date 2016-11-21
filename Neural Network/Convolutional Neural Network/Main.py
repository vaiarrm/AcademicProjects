#######################################################################################################################
# FILE: Main.py
# AUTHOR : Vaibhav Sharma
# DATE: 11/08/2016
# DESCRIPTION:
#######################################################################################################################

import Constants
import DataLoader
from ApplicationExceptions import TooManyInputException
import Network
from Validations import Validations

MAX_INPUT_ERROR_ALLOWED = 10
def main():
    """
    Application Starting Point
    :return:
    """
    print Constants.applicationAssumptions
    numInputErrors = 0

    # Gathering Data Set Information
    data = __getDataName(numInputErrors)
    dataInfo = data.getDataInfo()

    # Gathering Information about Convolutional Layers
    # Though code can handle multiple Convoluted Layers, running for just 1 as of now
    # If you want to run for multiple remove the next line and uncomment the method call
    numConvLayers = 1
    #numConvLayers = __getNumberOfConvultaitonalLayers(numInputErrors)
    cnvLayers = []

    for i in range(numConvLayers):
        cnvLayer = [dataInfo]
        print Constants.inputConvlutionalLayerNum, i+1

        localRecepDim = __getLocalReceptiveFieldDim(numInputErrors, dataInfo)
        cnvLayer.append(localRecepDim)

        padding = __getPadding(localRecepDim)
        cnvLayer.append(padding)

        stride = __getStride(numInputErrors, dataInfo, localRecepDim, padding)
        cnvLayer.append(stride)

        poolLayerDim = __getPoolDim(numInputErrors, dataInfo, localRecepDim, padding, stride)
        cnvLayer.append(poolLayerDim)

        outDepth = __getOuputDepth(numInputErrors)
        cnvLayer.append(outDepth)

        outDim = ((dataInfo[0] - localRecepDim + 2 * padding)/stride + 1)/poolLayerDim
        dataInfo = [outDim, outDim, outDepth]
        cnvLayer.append(dataInfo)
        cnvLayers.append(cnvLayer)

    # Gathering Information about Fully Connected Hidden Layer
    numFullConnectedLayer = __getNumberOfFullConnectedHiddenLayers(numInputErrors)
    fullLayers = []
    for i in range(numFullConnectedLayer):
        print Constants.inputFullHiddenLayerNum, i+1
        hiddenLayerSize = __getDimForFullHiddenLayer(numInputErrors)
        fullLayers.append(hiddenLayerSize)

    NNlayers = [cnvLayers,fullLayers]
    # Hyper Parameters
    numEpochs = 1
    eta = 0.3
    lmbda = 5

    n = Network.Network(NNlayers, data, numEpochs, eta, lmbda)
    print "Network Created Sucessfully"
    result = n.run()
    print "Done!!"

def __getDataName(numInputErrors):
    """
    Function asks the user for Data Set on whihc learning has to be done
    :return: Returns the DataLoader Object of specific Data set
    """

    while True:
        if numInputErrors  >  MAX_INPUT_ERROR_ALLOWED:
            raise TooManyInputException()
        try:
            dataChoice = input(Constants.inputDataChoice)
        except SyntaxError:
            continue
        if dataChoice == 1:
            data = DataLoader.MnistDataLoader()
            break
        elif dataChoice == 2:
            data = DataLoader.MnistRotated()
            break
        elif dataChoice == 3:
            data = DataLoader.MnistBackground()
            break
        elif dataChoice == 4:
            data = DataLoader.MnistRandomBackground()
            break
        elif dataChoice == 5:
            data = DataLoader.Cifar10DataLoader()
            break
        else:
            numInputErrors += 1
            print Constants.inputDataChoiceError
    return data



def __getNumberOfConvultaitonalLayers(numInputErrors):
    """
    Gets Number of Convulational Layer From the User
    :return: Number of Convulational Layers
    """
    while True:
        if numInputErrors  >  MAX_INPUT_ERROR_ALLOWED:
            raise TooManyInputException()
        try:
            noOfConvolutionalLayers = input(Constants.inputNoOfConvolutionalLayers)
        except SyntaxError:
            continue
        if not Validations.validateNumOfLayer(noOfConvolutionalLayers):
            numInputErrors += 1
            print Constants.inputNoOfConvolutionalLayersError
        else:
            return noOfConvolutionalLayers

def __getNumberOfFullConnectedHiddenLayers(numInputErrors):
    """
    :param numInputErrors:
    :return: Number of Fully Connected Hidden Layers
    """
    while True:
        if numInputErrors  >  MAX_INPUT_ERROR_ALLOWED:
            raise TooManyInputException()
        try:
            noOfFullLayers = input(Constants.inputNoOfFullHiddenlLayers)
        except SyntaxError:
            continue
        if not Validations.validateNumOfLayer(noOfFullLayers):
            numInputErrors += 1
            print Constants.inputNoOfFullHiddenlLayersError
        else:
            return noOfFullLayers

def __getDimForFullHiddenLayer(numInputErrors):
    """
    :param numInputErrors:
    :return: Number of Neurons in the Hidden Layer
    """
    while True:
        if numInputErrors  >  MAX_INPUT_ERROR_ALLOWED:
            raise TooManyInputException()
        try:
            numHiddenLayer = input(Constants.inputNumHiddenLayer)
        except SyntaxError:
            continue
        if not Validations.validateNumOfNeuronHiddenLayer(numHiddenLayer) :
            numInputErrors += 1
            print Constants.inputNumHiddenLayerError
        else:
            return numHiddenLayer

def __getLocalReceptiveFieldDim(numInputErrors,dataInfo):
    """
    Gets the dimension of Local Receptive Field
    :return: Returns the size of Local Receptive Field
    """

    while True:
        if numInputErrors  >  MAX_INPUT_ERROR_ALLOWED:
            raise TooManyInputException()
        try:
            localRecepDim = input(Constants.inputLocalReceptiveDim)
        except SyntaxError:
            continue
        if not Validations.validateLocalReceptiveField(dataInfo,localRecepDim):
            numInputErrors += 1
            print Constants.inputLocalReceptiveDimError
        else:
            return localRecepDim

def __getStride(numInputErrors,dataInfo,localRecepDim,padding):
    """
    :param numInputErrors:
    :param dataInfo:
    :param localRecepDim:
    :return: Stride value
    """
    while True:
        if numInputErrors  >  MAX_INPUT_ERROR_ALLOWED:
            raise TooManyInputException()
        try:
            stride = input(Constants.inputStride)
        except SyntaxError:
            continue
        if not Validations.validateStride(dataInfo,localRecepDim,padding,stride):
            numInputErrors += 1
            print Constants.inputStrideError
        else:
            return stride

def __getPoolDim(numInputErrors, dataInfo, localRecepDim, padding, stride):
    """
    :param numInputErrors:
    :param dataInfo:
    :param localRecepDim:
    :param stride:
    :return: Pool Size Dimension
    """
    while True:
        if numInputErrors  >  MAX_INPUT_ERROR_ALLOWED:
            raise TooManyInputException()
        poolDim = input(Constants.inputPool)
        if not Validations.validatePool(dataInfo, localRecepDim, padding, stride, poolDim):
            numInputErrors += 1
            print Constants.inputPoolError
        else:
            return poolDim

def __getOuputDepth(numInputErrors):
    """
    :param numInputErrors:
    :return: Depth of Output
    """

    while True:
        if numInputErrors  >  MAX_INPUT_ERROR_ALLOWED:
            raise TooManyInputException()
        try:
            outDepth = input(Constants.inputOutDepth)
        except SyntaxError:
            continue
        if not Validations.validateOutDepth(outDepth):
            numInputErrors += 1
            print Constants.inputOutDepthError
        else:
            return outDepth


def __getPadding(localRecepDim):
    """
    :param localRecepDim:
    :return: Padding
    """
    if localRecepDim == 3:
        return 1
    elif localRecepDim == 5:
        return 2
    elif localRecepDim == 3:
        return 3
    else:
        return 0


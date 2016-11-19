#######################################################################################################################
# FILE: Validations.py
# AUTHOR : Vaibhav Sharma
# DATE: 11/08/2016
# DESCRIPTION: Validation for all inputs
#######################################################################################################################


class Validations(object):

    @staticmethod
    def validateNumOfLayer(noOfLayers):
        if type(noOfLayers) != int or noOfLayers < 1:
            return False
        return True

    @staticmethod
    def validateLocalReceptiveField(dataInfo,localRecepDim):
        if type(localRecepDim) != int or localRecepDim not in {3, 5, 7} or dataInfo[0] < localRecepDim:
            return False
        return True

    @staticmethod
    def validateStride(dataInfo, localRecepDim, padding, stride):
        if type(stride) != int or stride < 1 or dataInfo[0] < stride or (dataInfo[0] - localRecepDim + 2 * padding) \
                % stride != 0:
            return False
        return True

    @staticmethod
    def validatePool(dataInfo, localRecepDim, padding, stride, poolDim):
        if type(poolDim) != int or poolDim < 1 or ((dataInfo[0] - localRecepDim + 2 * padding) / stride + 1) \
                % poolDim != 0:
            return False
        return True

    @staticmethod
    def validateNumOfNeuronHiddenLayer(numNeuron):
        if type(numNeuron) != int:
            return False
        return True

    @staticmethod
    def validateOutDepth(depth):
        if type(depth) != int:
            return False
        return True



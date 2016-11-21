#######################################################################################################################
# FILE: Constants.py
# AUTHOR : Vaibhav Sharma
# DATE: 11/08/2016
# DESCRIPTION: Lists all the string constants used in the application
#######################################################################################################################

# Menu Constants:
applicationAssumptions1 = '1. Shape of local receptive field is square\n2. zero-padding will be applied automatically\n'
applicationAssumptions2 = '3. Dimension of Output layer before pooling = (W - F + 2*P)/S + 1 where,\n\t W = Input Voume Size\n\t F = Receptive Field Size \n\t S = Stride\n\t P = Padding'
applicationAssumptions3 = ''
applicationAssumptions = applicationAssumptions1 + applicationAssumptions2 + applicationAssumptions3

inputDataChoice = 'Enter Data Set Choice\n\t1: MNIST Data\n\t2: MNIST Rotated\n\t3: MNIST with Background\n\t4: MNIST with Random Background\n\t5: CIFAR-10 Data\n'
inputDataChoiceError = 'Invalid Choice. Please enter 1 to 5 '

inputNoOfConvolutionalLayers = 'Enter Number of Convolutional Layers '
inputNoOfConvolutionalLayersError = 'Invalid Number. Should be integer with value greater than equal to 1 '

inputNoOfFullHiddenlLayers = 'Enter Number of Full Hidden Layers '
inputNoOfFullHiddenlLayersError = 'Invalid Number. Should be integer with value greater than equal to 1 '

inputConvlutionalLayerNum = 'Information for Convulational Layer '
inputFullHiddenLayerNum = 'Information for Full Hidden Layer '

inputLocalReceptiveDim = 'Enter Dimension For Local Receptive Field 3/5/7 '
inputLocalReceptiveDimError = 'Incorrect Dimension '

inputStride = 'Enter Stride '
inputStrideError = 'Incorrect Stride Value '

inputPool = 'Enter Pooling Dimensions '
inputPoolError = 'Incorrect Dimension '

inputOutDepth = 'Enter Number of Feature Maps '
inputOutDepthError = 'Invalid Feature Map Input '

inputNumHiddenLayer = 'Enter Number of Neurons in Hidden Layer '
inputNumHiddenLayerError = 'Invalid Number of Neurons '
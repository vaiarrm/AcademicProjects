###author: Vaibhav Sharma#Date: 10/08/2016#Description: Implements Backpropogation Algorithm#Arguments:  # a > input activation  # t > target values for activation  # batchsize > Size of mini batch  # eta > learning rate#Output:##function backpropogation(a,t,batchsize,eta,lambdaReg,mom,numofInstances)  #Global Variables - Initialized in start.m  global weights  global biases  global numberOfLayers   global cost  global correct  global transferFunc  global transferFuncDer  global correctCal  global costFunc    global calcLastDeltaVal      # For storing activation record at each layer  activations = cell(1,numberOfLayers);     # For storing z values at each layer  # zVals(1) is left uninitialized  zVals = cell(1,numberOfLayers);     # For storing delta values at each layer    # deltaVals(1) is left uninitialized  deltaVals = cell(1,numberOfLayers);     #weightsPd = cell(1,numberOfLayers);  #biasesPd = cell(1,numberOfLayers);   activations(1) = a;    # Calculation z and activation values for each layer starting from 2  # FIXED BIASES  for i = 2:numberOfLayers    w = weights{1,i};    b = biases{1,i};    b = repmat(b,1,batchsize);    z = w * a + b;        aTemp = transferFunc(z,i);    zVals(i) = z;    activations(i) = aTemp;    a = aTemp;  endfor    aLast = activations{1,numberOfLayers}; # Getting the final output  zLast = zVals{1,numberOfLayers}; # Getting the last z value    cost = cost + costFunc(t,aLast,lambdaReg);    correct = correct + correctCal(aLast,t);   deltaLast = calcLastDeltaVal(aLast,t,zLast);  deltaVals(numberOfLayers) = deltaLast;    # Caluclating delta values   # deltaVals(1) is left uninitialized  for i = numberOfLayers-1:-1:2    weightT = weights{1,i+1}';    deltaV = deltaVals{1,i+1};    zVal = zVals{1,i};        product = weightT * deltaV;    prime = transferFuncDer(zVal,i);    deltaVals(i) = product .* prime;      endfor  # Updating biases #FIXED BIASES  for i = 2: numberOfLayers    bPrev = biases{1,i}; # Old Weight for the layer    b = sum(deltaVals{1,i},2);    change = (eta/batchsize) * b;    biases(i) = bPrev - change;  endfor      # Updating weights #CORRECT  for i = 2: numberOfLayers    dVal = deltaVals{1,i}; # Delta value for the layer    aVal = activations{1,i-1}'; # Transpose of activation of the previous layer    product = dVal * aVal; # Matrix Multiplication    change = (eta/batchsize) * product; # Change for the layer    wPrev = weights{1,i}; # Old Weight for the layer    vel = mom * wPrev;    wPrev = (1 - eta*(lambdaReg/numofInstances))* wPrev;    wNew = wPrev - change + vel; # Calculating New Weight    weights(i) = wNew; # Updating new weights    #w{l} = (1-eta*(lmbda/numofInstances)) * w{l} - (eta/batchSize) + (mom * w{l});  endfor      endfunction
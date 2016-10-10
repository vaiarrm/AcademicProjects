###author: Vaibhav Sharma#Date: 10/08/2016#Description#Arguments:#  input > a matrix with a column for each example, and a row for each input feature#  target > a matrix with a column for each example, and a row for each output feature#  nodeLayers > a vector with the number of nodes in each layer (including the input and output layers).#  numEpochs > (scalar) desired number of epochs to run#  batchSize > (scalar) number of instances in a mini-batch#  eta > (scalar) learning rate#Output: Prints the output accuracy##function start(input,target,nodeLayers,numEpochs,batchSize,eta)    # Global varaibles that will be shared across multiple functions  global weights  global biases  global numberOfLayers  global attained100Accuracy    # Initializing Global Varaibles  numberOfLayers = columns(nodeLayers);  weights = cell(1,numberOfLayers);  biases  = cell(1,numberOfLayers);  attained100Accuracy = 0;  inputRows = rows(input);  inputCols = columns(input);  targetRows = rows(target);  targetColumns = columns(target);    ##    #Initialing weigths and biases with values with mean 0    #and standard devaiation 1        #Leaving the first index space blank. Starting from 2nd index   ##  #stdnormal_rnd  for i = 2: (numberOfLayers)    weights(i) = randn(nodeLayers(i),nodeLayers(i-1));     biases(i) = randn(nodeLayers(i),1);  endfor    for i = 2: (numberOfLayers)    b = biases{1,i};    b = repmat(b,1,batchSize);    biases(i) = b;  endfor  accuracies = ones(1,numEpochs);  mseV = zeros(1,numEpochs);  for i = 1:numEpochs      ##          #Randomly shuffling to remove any bias in the input data           # Not done in this version              #inputTemp = input'              #inputTemp = inputTemp(randperm(inputCols),:)              #input = inputTemp'      ##    for j = 1:batchSize:inputCols      a = input(:,[j:j+batchSize-1]);      t = target(:,[j:j+batchSize-1]);      backpropogation(a,t,batchSize,eta);      if (attained100Accuracy == 1)        break;      endif    endfor        #Calculating Accuracy    correctCount = 0;    mseSum = 0 ;    for k = 1:inputCols      a = input(:,[k:k]);      t = target(:,[k:k]);      for l = 2:numberOfLayers        w = weights{1,l};        b = biases{1,l};        z = z = w * a + b;        a = sigmoid(z);      endfor      a = round(a);      res = isequal(a,t);      if res == 1        correctCount++;      endif      diff = t - a;      diff = diff .* diff;      mseSum = mseSum + sum(diff);     endfor    MSE = mseSum /(2 * inputCols);    printf("Epoch %d: MSE: %f Correct: %d \ %d Accuracy: %f\n",i,MSE,correctCount,inputCols,correctCount/inputCols)    accuracies(i) = correctCount/inputCols;  endfor   plot(i= 1:numEpochs,accuracies(i));endfunction
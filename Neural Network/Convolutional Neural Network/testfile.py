import DataLoader
import Network

print "Starting Application"
inputDim = [28, 28, 1]
localRecepField = 5
padding = 2
stride = 1
maxPool = 2
featureMaps = 32
outputDim = [14, 14, 32]
hiddenLayer = [1024]

#l = [[[[28, 28, 1], 5, 2, 1, 2, 32, [14, 14, 32]]], [1024]]

Layers = [[[inputDim, localRecepField, padding, stride, maxPool, featureMaps, outputDim]], hiddenLayer]

# Uncomment the data you want to load

data = DataLoader.MnistDataLoader()
#data = DataLoader.MnistBackground()
#data = DataLoader.MnistRandomBackground()
#data = DataLoader.MnistRotated()
#data = DataLoader.MnistRotated()

# Hyper Parameters
numEpochs = 30
eta = 0.3
lmbda = 5

n = Network.Network(Layers, data, numEpochs, eta, lmbda)
print "Network Created Sucessfully"
result = n.run()

print "Processing Done"

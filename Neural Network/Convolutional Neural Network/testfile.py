import DataLoader
import Network

l = [[[[28, 28, 1], 5, 2, 1, 2, 32, [14, 14, 32]]], [1024]]
data = DataLoader.MnistDataLoader()
#data = DataLoader.MnistBackground()
#data = DataLoader.MnistRandomBackground()
#data = DataLoader.MnistRotated()
#data = DataLoader.MnistRotated()
numEpochs = 30
eta = 0.3
lmbda = 5
n = Network.Network(l, data, numEpochs, eta, lmbda)
print "Network Created Sucessfully"
result = n.run()


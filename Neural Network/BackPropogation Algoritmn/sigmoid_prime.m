###author: Vaibhav Sharma#Date: 10/08/2016#Description: Calculates the derivative sigmoid function#Output: Returns the value of derivative of sigmoid function##function [derVal] = sigmoid_prime(z)  derVal = sigmoid(z) .* (1 - sigmoid(z));endfunction
###author: Vaibhav Sharma#Date: 10/22/2016#Description: Calculates the Quadratic Cost#Output: Quadratic cost##function qCost = quadratic(t,a,lambdaReg)  qCost = (sum(sum((t - a).^2,2))) / 2;  qCost = qCost + + sumOfWeightsSquared(lambdaReg);endfunction
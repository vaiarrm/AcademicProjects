###author: Vaibhav Sharma#Date: 10/22/2016#Description: Calculates the Log Likelihood Cost#Output: Log Likelihood Cost##function lCost = log_cost(t,a,lambdaReg)    # y is row vector which contains the max index of each of the example  y = zeros(1,columns(t));    for i=1:columns(t)	  l = t(:,i:i);	  y(i)=find(l==max(l));  endfor    c = 0;    for i=1:columns(t)    temp = log(a(y(i),i));    if isfinite(temp) == 0		  temp = 0;  	endif    c = c + (-1 * temp);  endfor    lCost = c + sumOfWeightsSquared(lambdaReg);endfunction 
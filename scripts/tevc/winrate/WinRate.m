function fitness=WinRate(x)
d=length(x);
fitness=0;
for i=1:d
    fitness=fitness+x(i)*(2^(d-i));
end
fitness=fitness/(2^d-1);
end
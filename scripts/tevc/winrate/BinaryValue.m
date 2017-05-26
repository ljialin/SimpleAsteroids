function fitness=BinaryValue(x)
d=length(x);
fitness=0;
for i=1:d
    fitness=fitness+x(i)*(2^(d-i));
end
end
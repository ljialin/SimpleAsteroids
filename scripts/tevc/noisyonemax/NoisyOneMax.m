function res=NoisyOneMax(x,r)
res=OneMax(x)+randn/sqrt(r);
end
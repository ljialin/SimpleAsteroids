function fitness=NoisyWinRate(x)
if rand<=WinRate(x)
    fitness=1;
else
    fitness=0;
end
end
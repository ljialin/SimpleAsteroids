% (1+1)-EA
% RMHC
T=500;
runs=10;
R=20;
d=10;
p=1/d;
initPoints=InitPoints(d);
oporesults=zeros(R,runs*1024);
for r=1:R
    id=1;
    for run=1:runs
        for p=1:length(initPoints)
            x=initPoints(p,:);
            evals=0;
            while evals<T
                xp=x;
                for i=1:d
                    if rand<=p
                        xp(i)=1-xp(i);
                    end
                end
                fitnessX=0;
                fitnessXP=0;
                for rr=1:r
                    fitnessX=fitnessX+NoisyWinRate(x);
                    fitnessXP=fitnessXP+NoisyWinRate(xp);
                end
                
                if fitnessXP>=fitnessX
                    x=xp;
                end
                evals=evals+r*2;
            end
            %disp(sprintf('%d',OneMax(x)));
            oporesults(r,id)=WinRate(x);
            id=id+1;
        end
    end
end
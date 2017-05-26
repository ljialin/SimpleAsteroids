% RMHC
T=500;
runs=10;
R=20;
d=10;
initPoints=InitPoints(d);
rmhcresultsstop=zeros(R,runs*1024);
RMHCEVALS=zeros(R,runs*1024);

for r=1:R
    id=1;
    for run=1:runs
        for p=1:length(initPoints)
            x=initPoints(p,:);
            evals=0;
            findOpt=0;
            while findOpt==0
                xp=x;
                mut=randi(d);
                xp(mut)=1-xp(mut);
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
                if OneMax(x)==d
                    findOpt=1;
                end
            end
            %disp(sprintf('%d',OneMax(x)));
            rmhcresultsstop(r,id)=OneMax(x);
            RMHCEVALS(r,id)=evals;
            id=id+1;
        end
    end
end
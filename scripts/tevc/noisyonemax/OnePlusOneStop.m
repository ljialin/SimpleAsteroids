% (1+1)-EA
% RMHC
T=500;
runs=10;
R=20;
d=10;
prob=1/d;
initPoints=InitPoints(d);
oporesultsstop=zeros(R,runs);
OPOEVALS=zeros(R,runs);
for r=1:R
    id=1;
    for run=1:runs
        for p=1:length(initPoints)
            x=initPoints(p,:);
            evals=0;
            findOpt=0;

            while (findOpt==0)
                xp=x;
                for i=1:d
                    if rand<=prob
                        xp(i)=1-xp(i);
                    end
                end
                if NoisyOneMax(xp,r)>=NoisyOneMax(x,r)
                    x=xp;
                end
                evals=evals+r*2;
                if OneMax(x)==d
                    findOpt=1;
                end
            end
            %disp(sprintf('%d',OneMax(x)));
            oporesultsstop(r,id)=OneMax(x);
            OPOEVALS(r,id)=evals;
            id=id+1;
        end
    end
end
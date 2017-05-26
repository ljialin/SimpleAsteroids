% (1+1)-EA
% RMHC
T=500;
runs=10;
R=20;
d=10;
prob=1/d;
initPoints=InitPoints(d);
trials=10000;
opofaoresults=zeros(R,trials);
for r=1:R
    id=1;
    for run=1:runs
        for p=1:length(initPoints)
%             x=initPoints(p,:);
            x=RandomBinaryString(d);
            evals=0;
            while evals<T
                xp=x;
                mut=randi(d);
                xp(mut)=1-xp(mut);
                for i=1:d
                    if i~=mut
                        if rand<=prob
                            xp(i)=1-xp(i);
                        end
                    end
                end
               
                if NoisyOneMax(xp,r)>=NoisyOneMax(x,r)
                    x=xp;
                end
                evals=evals+r*2;
            end
            %disp(sprintf('%d',OneMax(x)));
            opofaoresults(r,id)=OneMax(x);
            id=id+1;
        end
    end
end
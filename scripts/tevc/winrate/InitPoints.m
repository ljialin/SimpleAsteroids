% Initial strings
function initPoints=InitPoints(d)
n=2^d;
initPoints=zeros(n,d);
for i=1:n
    res=i-1;
    for j=1:d
        initPoints(i,j)=floor(res/(2^(d-j)));
        res=mod(res,(2^(d-j)));
    end
end
end
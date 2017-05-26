function bits=RandomBinaryString(d)
    bits=zeros(1,d);
    for i=1:d
        if rand>0.5
            bits(i)=1;
        end
    end
end
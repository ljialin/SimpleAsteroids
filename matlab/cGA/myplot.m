mycolor={'b', 'r', 'g'};
mymap = [
    1 0.5 0.5;
    0 0 1;
    0 0 0];
for id=1:2
for w=[10 20 30 40 50]
figure
set(gca,'fontsize',18)
hold on
all={'RMHC', 'CGA', sprintf('NGA w=%d',w)};

h=zeros(1,3);
str='legend(h(1:3),';
if id==1
    M=load(sprintf('noisy_w%d.dat',w));
else
    M=load(sprintf('noisefree_w%d.dat',w));
end

runs=10;
for i=1:length(all)
    dataname=all{i};
    data=M((i-1)*runs+1:i*runs,:);
    [m,n]=size(data);
    mymean=mean(data);
    mystd=std(data)./sqrt(m);
    boundedline([1:n], mymean, mystd, 'cmap',mymap(i,:), 'alpha');

    h(i)=plot(mymean,'Color',mymap(i,:),'LineWidth',1);
    str=sprintf('%s ''%s'',',str,dataname);
end
eval(sprintf('%s  ''Location'', ''SouthEast'')',str));
axis([0 500 40 100])

if id==1
    title('Noisy OneMax: d=100, std(Noise)=1');
else
    title('OneMax: d=100');
end

xlabel('Evaluation number');
ylabel('Noise-free fitness');
grid on
if id==1
    saveas(gcf, sprintf('noisyonemaxd100_w%d.png',w));
else
    saveas(gcf, sprintf('onemaxd100_w%d.png',w));
end
end
end
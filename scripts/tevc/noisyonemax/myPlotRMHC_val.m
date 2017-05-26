% count and plot

figure
res=zeros(1,20);

hold on

runs=10000;

for i=1:20
    res(i)=mean(rmhcresults(i,1:runs));
end
plot(res,'ro-','LineWidth',2);
hold on

for i=1:20
    res(i)=mean(oporesults(i,1:runs));
end
plot(res,'bo-','LineWidth',2);


for i=1:20
    res(i)=mean(opofaoresults(i,1:runs));
end
plot(res,'go-','LineWidth',2);

xlabel('Resampling number','FontSize',14);
ylabel('Average noise-free fitness','FontSize',14);
legend('RMHC', '(1+1)-EA','(1+1)-EA, FAO')
title('T=500 fitness evaluations','FontSize',14);
set(gca,'FontSize',12);
print('pix/fitness_T500d10','-dpng')

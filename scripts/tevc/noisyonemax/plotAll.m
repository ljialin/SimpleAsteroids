% count and plot

figure
res=zeros(1,20);
for i=1:20
    res(i)=mean(oporesults(i,:));
end
plot(res,'bo-','LineWidth',3);
hold on
for i=1:20
    res(i)=mean(rmhcresults(i,:));
end
plot(res,'ro-','LineWidth',3);
hold on
%boundedline([1:20], res, err, 'alpha');

xlabel('Resampling number','FontSize',14);
ylabel('Average noise-free fitness','FontSize',14);
legend('(1+1)-EA','RMHC')
title('T=500 fitness evaluations','FontSize',14);
set(gca,'FontSize',12);
print('pix/fitness_T500d10','-dpng')

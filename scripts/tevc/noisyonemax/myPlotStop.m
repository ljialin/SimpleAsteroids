% count and plot

figure
plot(mean(RMHCEVALS(:,1:10230),2),'ro-','LineWidth',2);
hold on

err=zeros(1,20);
for i=1:20
    err(i)=std(RMHCEVALS(i,1:10230))/sqrt(10230)
end
errorbar([1:20],mean(RMHCEVALS(:,1:10230),2), err)

plot(mean(OPOEVALS(:,1:10230),2),'bo-','LineWidth',2);
err=zeros(1,20);
for i=1:20
    err(i)=std(OPOEVALS(i,1:10230))/sqrt(10230)
end
errorbar([1:20],mean(OPOEVALS(:,1:10230),2), err)

plot(mean(OPOFAOEVALS(:,1:10230),2),'go-','LineWidth',2);
err=zeros(1,20);
for i=1:20
    err(i)=std(OPOFAOEVALS(i,1:10230))/sqrt(10230)
end
errorbar([1:20],mean(OPOFAOEVALS(:,1:10230),2), err)

xlabel('Resampling number','FontSize',14);
ylabel('Fitness evaluations','FontSize',14);
legend('RMHC','(1+1)-EA','(1+1)-EA, FAO','Location','SouthEast')
title('First Hitting Time (FHT)','FontSize',14);

set(gca,'FontSize',12);
%print('RMHC_findOpt','-dpng')
%print('pix/findOpt_stopd10','-dpng')

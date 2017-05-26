% count and plot

figure
res=zeros(1,20);
err=zeros(1,20);
for i=1:20
    res(i)=mean(oporesults(i,:));
    err(i)=std(oporesults(i,:))./sqrt(10240);

end
plot(res,'bo-','LineWidth',3);
hold on
%boundedline([1:20], res, err, 'alpha');
%%%%%%%%%%%%%%
for i=1:20
    res(i)=mean(rmhcresults(i,:));
    err(i)=std(rmhcresults(i,:))./sqrt(10240);

end
plot(res,'ro-','LineWidth',3);
hold on
%boundedline([1:20], res, err, 'alpha');

xlabel('Resampling number','FontSize',14);
ylabel('Average noise-free fitness','FontSize',14);
legend('(1+1)-EA','RMHC')
title('Winning Rate Prediction, T=500 fitness evaluations','FontSize',14);
set(gca,'FontSize',12);
print('pix/trueFitness_T500d10','-dpng')

% count and plot

size(results)
findOptimumOPO=zeros(1,20);
for i=1:20
    findOptimumOPO(i)=length(find(oporesults(i,:)==1));
end
figure
plot(findOptimumOPO,'bo-','LineWidth',3);
hold on

findOptimumRMHC=zeros(1,20);
for i=1:20
    findOptimumRMHC(i)=length(find(rmhcresults(i,:)==1));
end

plot(findOptimumRMHC,'ro-','LineWidth',3);

xlabel('Resampling number','FontSize',14);
ylabel('Nb. Optimum found','FontSize',14);
legend('(1+1)-EA','RMHC', 'Location', 'NorthWest')
%title('RMHC','FontSize',14);
title('Winning Rate Prediction, T=500 fitness evaluations','FontSize',14);

set(gca,'FontSize',12);
%print('RMHC_findOpt','-dpng')
print('pix/findOptimumRate_T500d10','-dpng')

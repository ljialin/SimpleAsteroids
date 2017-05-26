% count and plot

findOptimumRMHC=zeros(1,20);
for i=1:20
    findOptimumRMHC(i)=length(find(rmhcresults(i,1:10230)==10));
end
figure

plot(findOptimumRMHC./10230*100,'ro-','LineWidth',2);
hold on

findOptimumOPO=zeros(1,20);
for i=1:20
    findOptimumOPO(i)=length(find(oporesults(i,1:10230)==10));
end
plot(findOptimumOPO./10230*100,'bo-','LineWidth',2);



findOptimumFAO=zeros(1,20);
for i=1:20
    findOptimumFAO(i)=length(find(opofaoresults(i,1:10230)==10));
end

plot(findOptimumFAO./10230*100,'go-','LineWidth',2);


xlabel('Resampling number','FontSize',14);
ylabel('Optimum found (%)','FontSize',14);
legend('RMHC', '(1+1)-EA','(1+1)-EA, FAO')
%title('RMHC','FontSize',14);
title('T=500 fitness evaluations','FontSize',14);

set(gca,'FontSize',12);
%print('RMHC_findOpt','-dpng')
print('pix/findOpt_T500d10','-dpng')

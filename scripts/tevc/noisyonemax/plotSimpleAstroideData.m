r=[1 5 10]
res1=[1 2 3]
res2=[4 5 6]
figure


plot(r,res1,'bo-','LineWidth',3);
hold on

plot(r,res2,'ro-','LineWidth',3);

xlabel('Resampling number','FontSize',14);
ylabel('Average noise-free fitness','FontSize',14);
legend('(1+1)-EA','RMHC')
title('Stop when finding optimal solution','FontSize',14);
set(gca,'FontSize',12);
print('pix/simpleAstroide','-dpng')

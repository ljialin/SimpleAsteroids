figure

range=[-20:20];
plot(range,range.^2/10,'b','LineWidth',2);
hold on

x=10;
y=20;
r=10;

plot(x,y,'r*')

R=[0.1 0.15 0.2 0.3 0.4 0.5 0.7 1 1.5 2 3 5 8]
for r=R
circle(x,y,r)
end

gaussian=y+2.*randn(100,1);
xx=[-100:100]/40;
norm = normpdf(xx,0,1);
plot((xx*40)/10+10,(norm)*10+y, 'r','LineWidth',2)
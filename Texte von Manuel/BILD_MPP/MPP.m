clc, clear all;

u = linspace(0,1,1000);

%% MPP 40%
offset = 0.4;
p = -(u-offset).^2;

for n = 1:(1000*offset)
    p1(n) = (1/p(1))*((n/1000-offset)^2)+1;
end
for n = (1000*offset+1):1000
    p1(n) = (1/p(1000))*((n/1000-offset)^2)+1;
end

%% MPP 50%
offset = 0.5;
p = -(u-offset).^2;

for n = 1:(1000*offset)
    p2(n) = (1/p(1))*((n/1000-offset)^2)+1;
end
for n = (1000*offset+1):1000
    p2(n) = (1/p(1000))*((n/1000-offset)^2)+1;
end

%% MPP 60%

offset = 0.6;
p = -(u-offset).^2;

for n = 1:(1000*offset)
    p3(n) = (1/p(1))*((n/1000-offset)^2)+1;
end
for n = (1000*offset+1):1000
    p3(n) = (1/p(1000))*((n/1000-offset)^2)+1;
end

%% MPP 70%

offset = 0.7;
p = -(u-offset).^2;

for n = 1:(1000*offset)
    p4(n) = (1/p(1))*((n/1000-offset)^2)+1;
end
for n = (1000*offset+1):1000
    p4(n) = (1/p(1000))*((n/1000-offset)^2)+1;
end

%% Ausgabe
plot(u,p1,u,p2,u,p3,u,p4)
axis([0 1 0 1])
grid minor
legend('MPP 1','MPP 2', 'MPP 3', 'MPP 4')
xlabel('Spannung (normalisiert)')
ylabel('Leistung (normalisiert)')
title('beispielhafte Entwicklung MPP')

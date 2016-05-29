clc, clear all;

u = linspace(0,1,1000);

%% MPP 10km/h
offset = 0.454;
p = -(u-offset).^2;

for n = 1:(1000*offset)
    p1(n) = (1/p(1))*((n/1000-offset)^2)+1;
end
for n = (1000*offset+1):1000
    p1(n) = (1/p(1000))*((n/1000-offset)^2)+1;
end

%% MPP 15km/h
offset = 0.377;
p = -(u-offset).^2;

for n = 1:(1000*offset)
    p2(n) = (1/p(1))*((n/1000-offset)^2)+1;
end
for n = (1000*offset+1):1000
    p2(n) = (1/p(1000))*((n/1000-offset)^2)+1;
end

%% MPP 20km/h

offset = 0.541;
p = -(u-offset).^2;

for n = 1:(1000*offset)
    p3(n) = (1/p(1))*((n/1000-offset)^2)+1;
end
for n = (1000*offset+1):1000
    p3(n) = (1/p(1000))*((n/1000-offset)^2)+1;
end

%% MPP 40km/h

offset = 0.681;
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
legend('10km/h','15km/h', '20km/h', '40km/h')
xlabel('Spannung (normalisiert)')
ylabel('Leistung (normalisiert)')
title('beispielhafte Entwicklung MPP')

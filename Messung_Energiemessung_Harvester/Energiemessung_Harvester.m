clc, clear all;

%R = [1,         10,    100,       1e3,    10e3,  100e3];
%I = [213.33e-6, 24e-6, 266.66e-6, 172e-6, 93e-6, 14e-6];
%R = [1,         10,    100,       1e3,    5e3,    8e3,     12e3,     10e3,  20e3,    50e3,    100e3];
%I = [213.33e-6, 24e-6, 266.66e-6, 172e-6, 114e-6, 92.5e-6, 70.83e-6, 93e-6, 55.5e-6, 23.5e-6, 14e-6];
R = [1,         10,    100,       1e3,    3e3,       5e3,    6e3,       7e3,      8e3,     12e3,     10e3,  20e3,    50e3,    100e3];
I = [213.33e-6, 24e-6, 266.66e-6, 172e-6, 153.33e-6, 114e-6, 108.33e-6, 98.57e-6, 92.5e-6, 70.83e-6, 93e-6, 55.5e-6, 23.5e-6, 14e-6];
P = I.^2 .* R;
U = R .* I;

figure(1)
[hAx,hLine1,hLine2] = plotyy(U, I, U, P);
grid minor;
title('Leistungs- und Widerstandskennlinie Harvester');
xlabel('Spannung [V]');
ylabel(hAx(1),'Strom [A]');
ylabel(hAx(2),'Leistung [W]');
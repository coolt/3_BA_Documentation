clc, clear all;

%% Vor dem Ausführen folgende Variabeln definiren

filename = 'Harvester_40kmh_9kOhm.csv'; % evtl. Dateiname anpassen
R = 9e3; % verwendeten Widerstand eintragen
v = 40; % eingestellte Geschwindigkeit eintragen

%% Laden der Daten aus dem CSV-File

data = csvread(filename, 16,1);
data_points = csvread(filename, 8, 1, [8, 1, 8, 1]);
x_resolution = csvread(filename, 5, 1, [5, 1, 5, 1]);

%% Variabeln berechnen

resolution = x_resolution * 10 / data_points;
period = 1/(v * 1000 / 3600 / 2.04);    
data_range = period / resolution;

%% Durchschnittliche Spannung, Strom, Leistung berechnen

u_mean = (sum(data(1:data_range)))/(data_range+1)
i_mean = u_mean / R
p_mean = i_mean * u_mean
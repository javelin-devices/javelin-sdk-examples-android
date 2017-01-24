# -*- coding: utf-8 -*-
"""
Created on Thu Nov 10 13:56:29 2016

@author: apwan_000
"""
##% Import Libraries and Grab JSON Data

import csv
import numpy as np
import json
import matplotlib.pyplot as plt

##% Specify Filepath 

with open('aaronpass') as data:
    d = json.load(data)
    data.close()

#%% Store JSON Data

acc_data = d["accelerometer_data"]
RawAccData = acc_data["data"]
sampling_rate_acc = acc_data["sampling_rate"]

gyro_data = d["gyroscope_data"]
RawGyroData = gyro_data["data"]
sampling_rate_gyro = gyro_data["sampling_rate"]

mag_data = d["magnetic_field_data"]
RawMagData = mag_data["data"]
#sampling_rate_mag = mag_data["sampling_rate"]
sampling_rate_mag = 20

temp_data = d["temperature_data"]
RawTempData = temp_data["data"]
sampling_rate_temp = temp_data["sampling_rate"]

audio_data = d["audio_energy_data"]
RawAudioData = audio_data["data"]
sampling_rate_audio = audio_data["sampling_rate"]

#%% Convert JSON Data to Array and Create Time Reference

RawAccData = np.array(RawAccData)
RawGyroData = np.array(RawGyroData)
RawMagData = np.array(RawMagData)
RawAudioData = np.array(RawAudioData)
RawTempData = np.array(RawTempData)

AccX = RawAccData[:,0]
AccY = RawAccData[:,1]
AccZ = RawAccData[:,2]

GyroX = RawGyroData[:,0]
GyroY = RawGyroData[:,1]
GyroZ = RawGyroData[:,2]

MagX = RawMagData[:,0]
MagY = RawMagData[:,1]
MagZ = RawMagData[:,2]

length_Acc_Data = len(RawAccData)
length_Gyro_Data = len(RawGyroData)
length_Mag_Data = len(RawMagData)
length_Audio_Data = len(RawAudioData)
length_Temp_Data = len(RawTempData)
length_Max = max(length_Acc_Data,length_Gyro_Data,length_Mag_Data,
                 length_Audio_Data,length_Temp_Data)

AccTime =  np.arange(0,length_Acc_Data/sampling_rate_acc,1/sampling_rate_acc)
GyroTime = np.arange(0,length_Gyro_Data/sampling_rate_gyro,
                     1/sampling_rate_gyro)
MagTime = np.arange(0,length_Mag_Data/sampling_rate_mag,1/sampling_rate_mag)
AudioTime = np.arange(0,length_Audio_Data/sampling_rate_audio,
                      1/sampling_rate_audio)
TempTime = np.arange(0,length_Temp_Data/sampling_rate_temp,
                     1/sampling_rate_temp)
    
#%% Plot Data

plt.close('all')

plt.figure(1)
plt.subplot(211)
plt.plot(AccTime,AccX,'r',AccTime,AccY,'g',AccTime,AccZ,'b')
plt.title('Raw Accelerometer Data')
plt.xlabel('Time (s)')
plt.ylabel('(g)')
plt.xticks(np.arange(0, length_Acc_Data/sampling_rate_acc, 1))


plt.subplot(212)
plt.plot(GyroTime,GyroX,'r',GyroTime,GyroY,'g',GyroTime,GyroZ,'b')
plt.title('Raw Gyroscope Data')
plt.xlabel('Time (s)')
plt.ylabel('Radians/Second')
plt.xticks(np.arange(0, length_Gyro_Data/sampling_rate_gyro, 1))


plt.figure(2)
plt.plot(MagTime,MagX,'r',MagTime,MagY,'g',MagTime,MagZ,'b')
plt.title('Raw Magnetometer Data')
plt.xlabel('Time (s)')
plt.ylabel('Tesla')
plt.xticks(np.arange(0, length_Mag_Data/sampling_rate_mag,1))


plt.figure(3)
plt.subplot(211)
plt.plot(AudioTime,RawAudioData,'r')
plt.title('Raw Audio Data')
plt.xlabel('Time (s)')
plt.ylabel('dB')
plt.xticks(np.arange(0, length_Audio_Data/sampling_rate_audio, 1))


plt.subplot(212)
plt.plot(TempTime,RawTempData,'b')
plt.title('Raw Temperature Data')
plt.xlabel('Time (s)')
plt.ylabel('Degrees F')
plt.xticks(np.arange(0, length_Temp_Data/sampling_rate_temp, 1))



#%% Convert to .csv

with open('test_data.csv','w',newline='') as csvfile:
    write = csv.writer(csvfile,delimiter=',')
    write.writerow(['AccX','AccY','AccZ','GyroX','GyroY','GyroZ','MagX','MagY',
                    'MagZ','Audio','Temperature'])
    for i in range(0,length_Max):
        if i<length_Temp_Data:
            write.writerow((AccX[i],AccY[i],AccZ[i],GyroX[i],GyroY[i],GyroZ[i],
                    MagX[i],MagY[i],MagZ[i],RawAudioData[i],RawTempData[i]))
        elif i>=length_Temp_Data and i<length_Audio_Data:
            write.writerow((AccX[i],AccY[i],AccZ[i],GyroX[i],GyroY[i],GyroZ[i],
                    MagX[i],MagY[i],MagZ[i],RawAudioData[i],''))
        elif i>=length_Audio_Data and i<length_Mag_Data:
            write.writerow((AccX[i],AccY[i],AccZ[i],GyroX[i],GyroY[i],GyroZ[i],
                    MagX[i],MagY[i],MagZ[i],'',''))
        elif i>=length_Mag_Data and i<length_Gyro_Data:
            write.writerow((AccX[i],AccY[i],AccZ[i],GyroX[i],GyroY[i],GyroZ[i],
                            '','','','',''))
        elif i>=length_Gyro_Data and i<length_Acc_Data:
            write.writerow((AccX[i],AccY[i],AccZ[i],'','','','','','','',''))





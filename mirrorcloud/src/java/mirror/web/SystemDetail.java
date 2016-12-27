/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mirror.web;

/**
 *
 * @author ahmed
 */
public class SystemDetail {
    String machineName,osName,sysModel,cpu,memory,macAddress;
    int systemId,pcId;

    public int getSystemId() {
        return systemId;
    }

    public void setSystemId(int systemId) {
        this.systemId = systemId;
    }

    public int getPcId() {
        return pcId;
    }

    public void setPcId(int pcId) {
        this.pcId = pcId;
    }

    
    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getSysModel() {
        return sysModel;
    }

    public void setSysModel(String sysModel) {
        this.sysModel = sysModel;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }
    @Override public String toString()
    {
        String s = "";
        s+="Machine Name: "+getMachineName()+"\n";
        s+="Operating System: "+getOsName()+"\n";
        s+="System Model: "+getSysModel()+"\n";
        s+="Processor: "+getCpu()+"\n";
        s+="Memory: "+getMemory()+"\n";
        s+="MAC Address: "+getMacAddress()+"\n";
        return s;
    }    
}

package com.cajor.dk.dlna;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.DeviceDetails;
import org.fourthline.cling.model.meta.Icon;
import org.fourthline.cling.model.meta.ManufacturerDetails;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.Service;
import org.seamless.util.MimeType;

import java.net.URI;

public class DeviceModel extends CustomListItem {

    private final Device device;

    public DeviceModel(int icon, Device device) {
        super(icon);
        this.device = device;

        setIconUrl(getIconUrl());
    }

    public String getIconUrl() {
        for (Object o : device.getIcons()) {
            Icon icon = (Icon)o;

            if (icon == null)
                continue;

            if (icon.getWidth() >= 64 && icon.getHeight() >= 64
                    && isUsableImageType(icon.getMimeType()))
                return ((RemoteDevice)device).normalizeURI(icon.getUri()).toString();
        }
        return null;
    }

    private boolean isUsableImageType(MimeType mt) {
        return mt.getType().equals("image") &&
                (mt.getSubtype().equals("png") || mt.getSubtype().equals("jpg") ||
                        mt.getSubtype().equals("jpeg") || mt.getSubtype().equals("gif"));
    }

    public Device getDevice() {
        return device;
    }

    public Service getContentDirectory() {
        for (Service current : this.device.getServices())
            if (current.getServiceType().getType().equals("ContentDirectory"))
                return current;

        return null;
    }

    @Override
    public String getTitle() {
        return toString();
    }

    @Override
    public String getDescription() {
        DeviceDetails details = device.getDetails();
        if (details == null)
            return "N/A";

        ManufacturerDetails manDetails = details.getManufacturerDetails();
        if (manDetails == null)
            return "N/A";

        String manufacturer = manDetails.getManufacturer();
        if (manufacturer == null)
            return "N/A";

        return manufacturer;
    }

    @Override
    public String getDescription2() {
        DeviceDetails details = device.getDetails();
        if (details == null)
            return "N/A";

        ManufacturerDetails manDetails = details.getManufacturerDetails();
        if (manDetails == null)
            return "N/A";

        URI uri = manDetails.getManufacturerURI();
        if (uri == null)
            return "N/A";

        return uri.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        DeviceModel that = (DeviceModel)o;
        return device.equals(that.device);
    }

    @Override
    public int hashCode() {
        return device.hashCode();
    }

    @Override
    public String toString() {
        String name =
                getDevice().getDetails() != null
                        && getDevice().getDetails().getFriendlyName() != null
                        ? getDevice().getDetails().getFriendlyName()
                        : getDevice().getDisplayString();

        return device.isFullyHydrated() ? name : name + " *";
    }
}

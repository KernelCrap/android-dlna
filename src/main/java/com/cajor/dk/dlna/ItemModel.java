package com.cajor.dk.dlna;

import android.content.res.Resources;
import android.net.Uri;

import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.Item;

import java.util.List;

public class ItemModel extends CustomListItem {
    private final Resources res;
    private final Service service;
    private final DIDLObject item;
    private Boolean showExtension = false;

    public ItemModel(Resources res, int icon, Service service, DIDLObject item) {
        super(icon);

        this.res = res;
        this.service = service;
        this.item = item;
    }

    public String getUrl() {
        return item.getFirstResource().getValue();
    }

    public void setShowExtension(Boolean show) {
        this.showExtension = show;
    }

    public Item getItem() {
        if (isContainer())
            return null;

        return (Item)item;
    }

    public Container getContainer() {
        if (!isContainer())
            return null;

        return (Container)item;
    }

    public Service getService() {
        return this.service;
    }

    public boolean isContainer() {
        return item instanceof Container;
    }

    @Override
    public String getId() {
        return item.getId();
    }

    @Override
    public String getTitle() {
        if (showExtension)
            return item.getTitle() + "." + MimeTypeMap.getFileExtensionFromUrl(getUrl());

        return item.getTitle();
    }

    @Override
    public String getDescription() {
        if (isContainer()) {
            Integer children = getContainer().getChildCount();

            if (children != null)
                return getContainer().getChildCount() + " " + res.getString(R.string.info_items);

            return res.getString(R.string.info_folder);
        }

        List<Res> resources =  item.getResources();
        if (resources != null && resources.size() != 0) {

            Res resource = item.getResources().get(0);
            String resolution = resource.getResolution();

            if (resolution != null)
                return resolution;

            String creator = item.getCreator();
            if (creator == null)
                return res.getString(R.string.info_file);

            if (creator.startsWith("Unknown"))
                return null;

            return creator;
        }

        return "N/A";
    }

    @Override
    public String getDescription2() {
        if (!isContainer()) {
            Uri uri = Uri.parse(getUrl());
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            return mime.getMimeTypeFromUrl(uri.toString());
        }

        String genre = item.getFirstPropertyValue(DIDLObject.Property.UPNP.GENRE.class);
        if (genre == null)
            return null;

        if (genre.startsWith("Unknown"))
            return null;

        return genre;
    }
}

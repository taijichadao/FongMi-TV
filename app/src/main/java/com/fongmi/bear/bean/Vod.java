package com.fongmi.bear.bean;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.fongmi.bear.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Vod {

    @SerializedName("vod_id")
    private String vodId;
    @SerializedName("vod_name")
    private String vodName;
    @SerializedName("type_name")
    private String typeName;
    @SerializedName("vod_pic")
    private String vodPic;
    @SerializedName("vod_remarks")
    private String vodRemarks;
    @SerializedName("vod_year")
    private String vodYear;
    @SerializedName("vod_area")
    private String vodArea;
    @SerializedName("vod_director")
    private String vodDirector;
    @SerializedName("vod_actor")
    private String vodActor;
    @SerializedName("vod_content")
    private String vodContent;
    @SerializedName("vod_play_from")
    private String vodPlayFrom;
    @SerializedName("vod_play_url")
    private String vodPlayUrl;

    private List<Flag> vodFlags;

    public static Vod objectFrom(String str) {
        return new Gson().fromJson(str, Vod.class);
    }

    public String getVodId() {
        return TextUtils.isEmpty(vodId) ? "" : vodId;
    }

    public String getVodName() {
        return TextUtils.isEmpty(vodName) ? "" : vodName;
    }

    public String getTypeName() {
        return TextUtils.isEmpty(typeName) ? "" : typeName;
    }

    public String getVodPic() {
        return TextUtils.isEmpty(vodPic) ? "" : vodPic;
    }

    public String getVodRemarks() {
        return TextUtils.isEmpty(vodRemarks) ? "" : vodRemarks;
    }

    public String getVodYear() {
        return TextUtils.isEmpty(vodYear) ? "" : vodYear;
    }

    public String getVodArea() {
        return TextUtils.isEmpty(vodArea) ? "" : vodArea;
    }

    public String getVodDirector() {
        return TextUtils.isEmpty(vodDirector) ? "" : vodDirector;
    }

    public String getVodActor() {
        return TextUtils.isEmpty(vodActor) ? "" : vodActor;
    }

    public String getVodContent() {
        return TextUtils.isEmpty(vodContent) ? "" : vodContent.replaceAll("\\s+", "");
    }

    public String getVodPlayFrom() {
        return TextUtils.isEmpty(vodPlayFrom) ? "" : vodPlayFrom;
    }

    public String getVodPlayUrl() {
        return TextUtils.isEmpty(vodPlayUrl) ? "" : vodPlayUrl;
    }

    public List<Flag> getVodFlags() {
        return vodFlags = vodFlags == null ? new ArrayList<>() : vodFlags;
    }

    public int getRemarkVisible() {
        return getVodRemarks().isEmpty() ? View.GONE : View.VISIBLE;
    }

    public void loadImg(ImageView view) {
        if (TextUtils.isEmpty(getVodPic())) {
            String text = getVodName().isEmpty() ? "" : getVodName().substring(0, 1);
            view.setImageDrawable(TextDrawable.builder().buildRect(text, ColorGenerator.MATERIAL.getColor(text)));
        } else {
            Utils.loadImage(getVodPic(), view);
        }
    }

    public void setVodFlags() {
        String[] playFlags = getVodPlayFrom().split("\\$\\$\\$");
        String[] playUrls = getVodPlayUrl().split("\\$\\$\\$");
        for (int i = 0; i < playFlags.length; i++) {
            Vod.Flag item = new Vod.Flag(playFlags[i]);
            String[] urls = playUrls[i].contains("#") ? playUrls[i].split("#") : new String[]{playUrls[i]};
            for (String url : urls) {
                if (!url.contains("$")) continue;
                String[] split = url.split("\\$");
                if (split.length >= 2) item.getEpisodes().add(new Vod.Flag.Episode(split[0], split[1]));
            }
            getVodFlags().add(item);
        }
    }

    public static class Flag {

        @SerializedName("flag")
        private final String flag;
        @SerializedName("episodes")
        private final List<Episode> episodes;

        public static Flag objectFrom(String str) {
            return new Gson().fromJson(str, Flag.class);
        }

        public Flag(String flag) {
            this.flag = flag;
            this.episodes = new ArrayList<>();
        }

        public String getFlag() {
            return flag;
        }

        public List<Episode> getEpisodes() {
            return episodes;
        }

        public void deactivated() {
            for (Episode item : getEpisodes()) item.deactivated();
        }

        public void setActivated(Episode episode) {
            for (Episode item : getEpisodes()) item.setActivated(episode);
        }

        @NonNull
        @Override
        public String toString() {
            return new Gson().toJson(this);
        }

        public static class Episode {

            @SerializedName("name")
            private final String name;
            @SerializedName("url")
            private final String url;
            @SerializedName("activated")
            private boolean activated;

            public Episode(String name, String url) {
                this.name = name;
                this.url = url;
            }

            public String getName() {
                return name;
            }

            public String getUrl() {
                return url;
            }

            public boolean isActivated() {
                return activated;
            }

            public void setActivated(boolean activated) {
                this.activated = activated;
            }

            private void deactivated() {
                this.activated = false;
            }

            private void setActivated(Episode item) {
                this.activated = item.equals(this);
            }
        }
    }
}

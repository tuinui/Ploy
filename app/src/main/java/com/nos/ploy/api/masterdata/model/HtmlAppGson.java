package com.nos.ploy.api.masterdata.model;

import com.google.gson.annotations.SerializedName;
import com.nos.ploy.api.base.response.BaseResponse;

/**
 * Created by Saran on 25/12/2559.
 */

public class HtmlAppGson extends BaseResponse<HtmlAppGson.Data> {


    public static class Data {
        /*"data": {
           "lgCode": null,
           "policy": null,
           "term": null,
           "legal": null,
           "faq": null,
           "whatIsPloyee": null,
           "whatIsPloyer": "testsdfa"
           }
           */
        @SerializedName("lgCode")
        private String lgCode;
        @SerializedName("policy")
        private String policy;
        @SerializedName("term")
        private String term;
        @SerializedName("legal")
        private String legal;
        @SerializedName("faq")
        private String faq;
        @SerializedName("whatIsPloyee")
        private String whatIsPloyee;
        @SerializedName("whatIsPloyer")
        private String whatIsPloyer;

        public Data() {
        }

        public String getLgCode() {
            return lgCode;
        }

        public String getPolicy() {
            return policy;
        }

        public String getTerm() {
            return term;
        }

        public String getLegal() {
            return legal;
        }

        public String getFaq() {
            return faq;
        }

        public String getWhatIsPloyee() {
            return whatIsPloyee;
        }

        public String getWhatIsPloyer() {
            return whatIsPloyer;
        }
    }

}

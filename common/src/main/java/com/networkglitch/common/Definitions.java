package com.networkglitch.common;

public final class Definitions {
    public static final class ConfigKeys {
        public static final String DEBUG = "debug";
        public static final String FirstPlayerJoinedEnabled = "first_join_message_enabled";
        public static final String PlayerJoinedEnabled = "join_message_enabled";
        public static final String PlayerLeftEnabled = "leave_message_enabled";
        public static final String PlayerRenamedEnabled = "rename_message_enabled";

        public static final String CustomFirstJoinMessageEnabled = "custom_first_join_message";
        public static final String CustomJoinMessageEnabled = "custom_join_message";
        public static final String CustomLeaveMessageEnabled = "custom_leave_message";
        public static final String CustomRenameMessageEnabled = "custom_rename_message";

        public static final String CustomFirstJoinMessageText = "custom_first_join_message_text";
        public static final String CustomJoinMessageText = "custom_join_message_text";
        public static final String CustomLeaveMessageText = "custom_leave_message_text";
        public static final String CustomRenameMessageText = "custom_rename_message_text";



        public static final String PrivateFirstJoinEnabled = "private_first_join_message";
        public static final String PrivateJoinEnabled = "private_join_message";
        public static final String PrivateFirstJoinMessageText = "private_first_join_message_text";
        public static final String PrivateJoinMessageText = "private_join_message_text";
    }


    public static final class MessageKeys {
        public static final String PlayerRenamed = "multiplayer.player.joined.renamed";
        public static final String PlayerJoined = "multiplayer.player.joined";
        public static final String PlayerLeft = "multiplayer.player.left";
    }

    public static class SendMessageResponse {

        private Boolean SendMessage = true;
        private String TranslateKey = null;
        private String CustomMessage = null;

        public SendMessageResponse(Boolean sendMessage) {
            SendMessage = sendMessage;
        }

        public SendMessageResponse(String translateKey, String customMessage) {
            TranslateKey = translateKey;
            CustomMessage = customMessage;
        }

        public String getCustomMessage() {
            return CustomMessage;
        }

        public String getTranslateKey() {
            return TranslateKey;
        }

        public Boolean getSendMessage() {
            return SendMessage;
        }
    }
}
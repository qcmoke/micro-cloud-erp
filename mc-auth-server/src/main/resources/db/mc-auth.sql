/*
 Navicat Premium Data Transfer

 Source Server         : aliyun-mysql
 Source Server Type    : MySQL
 Source Server Version : 50564
 Source Host           : 39.106.195.202:3306
 Source Schema         : mc-auth

 Target Server Type    : MySQL
 Target Server Version : 50564
 File Encoding         : 65001

 Date: 10/02/2020 14:43:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oauth_access_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_access_token`;
CREATE TABLE `oauth_access_token`  (
  `token_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `token` blob NULL,
  `authentication_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `client_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `authentication` blob NULL,
  `refresh_token` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`authentication_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for oauth_approvals
-- ----------------------------
DROP TABLE IF EXISTS `oauth_approvals`;
CREATE TABLE `oauth_approvals`  (
  `userId` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `clientId` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `scope` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `expiresAt` datetime NULL DEFAULT NULL,
  `lastModifiedAt` datetime NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`  (
  `client_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '认证客户端id',
  `resource_ids` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '认证客户端能访问的资源服务器列表。表示client_id申请得到的token能访问的资源服务器id集合，多个用逗号隔开，如果可以访问所有则设置为空即可',
  `client_secret` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '认证客户端秘钥',
  `scope` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '针对认证客户端应用的权限范围而非用户权限，即client_id对resource_ids拥有的权限。如果范围未定义或为空，即默认，客户端将不受范围限制。',
  `authorized_grant_types` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权模式类型',
  `web_server_redirect_uri` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权码模式中回调地址',
  `authorities` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `access_token_validity` int(11) NULL DEFAULT NULL COMMENT '令牌有限期',
  `refresh_token_validity` int(11) NULL DEFAULT NULL COMMENT '刷新令牌有限期',
  `additional_information` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `autoapprove` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否自动授予认证客户端(非用户)对资源服务器的所有权限',
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details` VALUES ('admin', '', '$2a$10$Jh7dVwn/X86L89DIiqT5ROaWAzL1.fRLhVQBVUlLx6HfXpDsHTy7y', 'all', 'password,authorization_code', 'http://127.0.0.1:80/admin/callback', NULL, 3600, NULL, NULL, 'true');
INSERT INTO `oauth_client_details` VALUES ('gateway', '', '$2a$10$Jh7dVwn/X86L89DIiqT5ROaWAzL1.fRLhVQBVUlLx6HfXpDsHTy7y', 'all', 'password', NULL, NULL, 3600, NULL, NULL, NULL);
INSERT INTO `oauth_client_details` VALUES ('orderApp', '', '$2a$10$Jh7dVwn/X86L89DIiqT5ROaWAzL1.fRLhVQBVUlLx6HfXpDsHTy7y', 'read,write', 'password,refresh_token', NULL, NULL, 3600, NULL, NULL, NULL);
INSERT INTO `oauth_client_details` VALUES ('orderServer', '', '$2a$10$Jh7dVwn/X86L89DIiqT5ROaWAzL1.fRLhVQBVUlLx6HfXpDsHTy7y', 'read', 'password', NULL, NULL, 3600, NULL, NULL, NULL);
INSERT INTO `oauth_client_details` VALUES ('stockServer', '', '$2a$10$Jh7dVwn/X86L89DIiqT5ROaWAzL1.fRLhVQBVUlLx6HfXpDsHTy7y', 'all', 'password', NULL, NULL, 3600, NULL, NULL, NULL);
INSERT INTO `oauth_client_details` VALUES ('swagger', '', '$2a$10$Jh7dVwn/X86L89DIiqT5ROaWAzL1.fRLhVQBVUlLx6HfXpDsHTy7y', 'all', 'password,refresh_token', NULL, NULL, 3600, NULL, NULL, NULL);
INSERT INTO `oauth_client_details` VALUES ('webApp', '', '$2a$10$Jh7dVwn/X86L89DIiqT5ROaWAzL1.fRLhVQBVUlLx6HfXpDsHTy7y', 'all', 'password,refresh_token', NULL, NULL, 3600, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for oauth_client_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_token`;
CREATE TABLE `oauth_client_token`  (
  `token_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `token` blob NULL,
  `authentication_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `client_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`authentication_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for oauth_code
-- ----------------------------
DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code`  (
  `code` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `authentication` blob NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for oauth_refresh_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_refresh_token`;
CREATE TABLE `oauth_refresh_token`  (
  `token_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `token` blob NULL,
  `authentication` blob NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;

/*
 Navicat Premium Data Transfer

 Source Server         : aliyun-mysql
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : 39.106.195.202:3306
 Source Schema         : mc-wms

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 17/03/2020 14:40:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_material_stock
-- ----------------------------
DROP TABLE IF EXISTS `t_material_stock`;
CREATE TABLE `t_material_stock`  (
  `materiel_stock_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '原料库存编号',
  `material_id` bigint(20) NULL DEFAULT NULL COMMENT '原料编号',
  `material_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '原料名称',
  `area` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '存放地点',
  `count` double(20, 0) NULL DEFAULT NULL COMMENT '库存量',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态【0->未删除；1->已删除】',
  PRIMARY KEY (`materiel_stock_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_material_stock
-- ----------------------------
INSERT INTO `t_material_stock` VALUES (3, 1, '骁龙850', NULL, 74, '2020-03-14 03:45:24', '2020-03-17 07:14:25', 0);
INSERT INTO `t_material_stock` VALUES (4, 1, '骁龙850', NULL, 11, '2020-03-14 03:45:24', NULL, 0);
INSERT INTO `t_material_stock` VALUES (5, 2, '麒麟960', NULL, 558, '2020-03-14 03:45:24', '2020-03-17 07:14:25', 0);
INSERT INTO `t_material_stock` VALUES (6, 3, '联发科x30', NULL, 3, '2020-03-17 04:13:28', '2020-03-17 07:14:25', 0);
INSERT INTO `t_material_stock` VALUES (7, 5, '京东方 td4310屏', NULL, 3, '2020-03-17 04:13:28', NULL, 0);
INSERT INTO `t_material_stock` VALUES (8, 6, '索尼2000万光学后置摄像头', NULL, 10, '2020-03-17 04:13:28', NULL, 0);
INSERT INTO `t_material_stock` VALUES (9, 4, '英特尔酷睿i5-4210m', NULL, 1, '2020-03-17 07:14:25', NULL, 0);

-- ----------------------------
-- Table structure for t_product_stock
-- ----------------------------
DROP TABLE IF EXISTS `t_product_stock`;
CREATE TABLE `t_product_stock`  (
  `product_stock_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '产品库存编号',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '产品编号',
  `product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品名称',
  `area` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '存放地点',
  `count` double(20, 0) NULL DEFAULT NULL COMMENT '库存量',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态【0->未删除；1->已删除】',
  PRIMARY KEY (`product_stock_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

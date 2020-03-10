/*
 Navicat Premium Data Transfer

 Source Server         : aliyun-mysql
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : 39.106.195.202:3306
 Source Schema         : mc-oms

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 11/03/2020 00:15:55
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_customer
-- ----------------------------
DROP TABLE IF EXISTS `t_customer`;
CREATE TABLE `t_customer`  (
  `customer_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '客户编号',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户收收货地址',
  `bank` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户行',
  `bank_account` bigint(20) NULL DEFAULT NULL COMMENT '银行账户',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电子邮件',
  `link_man` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `link_tel` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人电话',
  `customer_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户名称',
  `tel_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `postcode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT NULL COMMENT '删除状态',
  PRIMARY KEY (`customer_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_customer
-- ----------------------------
INSERT INTO `t_customer` VALUES (1, '江西省南昌市昌北经开区', '南昌银行', 137347874478935, 'abc@123.com', '王琦', '123456789', 'XXX电子上午有限公司', '0790-7378785789', '3274837', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for t_product
-- ----------------------------
DROP TABLE IF EXISTS `t_product`;
CREATE TABLE `t_product`  (
  `product_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '产品编号',
  `product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '产品名称',
  `approve_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '批准文号',
  `batch_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生产批号',
  `price` double NULL DEFAULT NULL COMMENT '单价',
  `space` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品产地',
  `supplier_id` bigint(20) NULL DEFAULT NULL COMMENT '供应商编号',
  `unit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单位',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT NULL COMMENT '删除状态',
  PRIMARY KEY (`product_id`) USING BTREE,
  UNIQUE INDEX `UK_8nlyljvfo33wg4ei3nx2fujdr`(`product_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_product
-- ----------------------------
INSERT INTO `t_product` VALUES (1, 'apple', '13243434564', '65454213113', 6777, '上海', 121323, '台', NULL, NULL, NULL);
INSERT INTO `t_product` VALUES (2, '魅族', '13243434564', '65454213113', 3333, '珠海', 12332, '台', NULL, NULL, NULL);
INSERT INTO `t_product` VALUES (5, '小米', '13243434564', '65454213113', 3333, '北京', 12332, '台', NULL, NULL, NULL);
INSERT INTO `t_product` VALUES (6, '魅蓝', '13243434564', '65454213113', 1222, '珠海', 121323, '台', NULL, NULL, NULL);
INSERT INTO `t_product` VALUES (7, 'apple6s', '13243434564', '65454213113', 6777, '上海', 121323, '台', NULL, NULL, NULL);
INSERT INTO `t_product` VALUES (8, 'apple7', '13243434564', '65454213113', 6777, '珠海', 121323, '台', NULL, NULL, NULL);
INSERT INTO `t_product` VALUES (9, 'iphone5s', '13243434564', '65454213113', 6777, '上海', 121323, '台', NULL, NULL, NULL);
INSERT INTO `t_product` VALUES (10, '魅蓝4', '13243434564', '65454213113', 1222, '珠海', 121323, '台', NULL, NULL, NULL);
INSERT INTO `t_product` VALUES (11, '魅族6', '13243434564', '65454213113', 3333, '珠海', 121323, '台', NULL, NULL, NULL);
INSERT INTO `t_product` VALUES (12, 'iphone7p', '13243434564', '65454213113', 6777, '北京', 121323, '台', NULL, NULL, NULL);
INSERT INTO `t_product` VALUES (13, '魅族6p', '13243434564', '65454213113', 3333, '珠海', 121323, '台', NULL, NULL, NULL);
INSERT INTO `t_product` VALUES (14, '小米6s', '13243434564', '65454213113', 3333, '珠海', 121323, '台', NULL, NULL, NULL);
INSERT INTO `t_product` VALUES (15, 'opple', '13243434564', '65454213113', 2333, '上海', 12332, '台', NULL, NULL, NULL);
INSERT INTO `t_product` VALUES (16, 'vivo Xplay7', '13243434564', '65454213113', 2333, '珠海', 1001, '台', NULL, NULL, NULL);
INSERT INTO `t_product` VALUES (17, 'iphone7ps', '13243434564', '65454213113', 6777, '上海', 1001, '台', NULL, NULL, NULL);
INSERT INTO `t_product` VALUES (18, '魅蓝note', '13243434564', '65454213113', 1222, '珠海', 1001, '台', NULL, NULL, NULL);
INSERT INTO `t_product` VALUES (19, 'iPhone4s', '13243434564', '65454213113', 4567, '上海', 1001, '台', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for t_sale_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_sale_order_detail`;
CREATE TABLE `t_sale_order_detail`  (
  `detail_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '销售订单明细表编号',
  `master_id` bigint(20) NULL DEFAULT NULL COMMENT '销售订单主表编号',
  `product_id` bigint(20) NULL DEFAULT NULL COMMENT '产品编号',
  `customer_id` bigint(20) NULL DEFAULT NULL COMMENT '客户编号',
  `price` double NULL DEFAULT NULL COMMENT '单价',
  `count` bigint(20) NULL DEFAULT NULL COMMENT '数量',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT NULL COMMENT '删除状态',
  PRIMARY KEY (`detail_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sale_order_master
-- ----------------------------
DROP TABLE IF EXISTS `t_sale_order_master`;
CREATE TABLE `t_sale_order_master`  (
  `master_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '销售订单主表编号',
  `customer_id` bigint(20) NULL DEFAULT NULL COMMENT '客户编号',
  `sale_date` datetime(0) NULL DEFAULT NULL COMMENT '销售时间',
  `total_amount` double NULL DEFAULT NULL COMMENT '订单总金额',
  `freight_amount` double NULL DEFAULT NULL COMMENT '运费金额',
  `pay_type` tinyint(4) NULL DEFAULT NULL COMMENT '支付方式【1->支付宝；2->微信；3->银联； 4->货到付款；】',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '订单状态【0->待付款；1->待发货；2->已发货；3->未确认收货；4->已确认收货；5->已完成；6->已关闭；7->无效订单】',
  `delivery_time` datetime(0) NULL DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime(0) NULL DEFAULT NULL COMMENT '收货人确认收货时间',
  `receiver_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人电话号码',
  `receiver_post_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人邮编',
  `receiver_province` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人所在省',
  `receiver_city` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人所在城市',
  `receiver_region` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人所在区',
  `receiver_detail_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货人所在详细地址',
  `delivery_sn` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '物流单号',
  `bill_type` tinyint(4) NULL DEFAULT NULL COMMENT '发票类型[0->不开发票；1->电子发票；2->纸质发票]',
  `bill_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发票内容',
  `bill_header` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发票抬头',
  `bill_receiver_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收票人电话',
  `bill_receiver_email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收票人邮箱',
  `payment_time` datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT NULL COMMENT '删除状态【0->未删除；1->已删除】',
  `note` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单备注',
  PRIMARY KEY (`master_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_sale_refund
-- ----------------------------
DROP TABLE IF EXISTS `t_sale_refund`;
CREATE TABLE `t_sale_refund`  (
  `refund_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '销售退单编号',
  `sale_order_master_id` bigint(20) NULL DEFAULT NULL COMMENT '订单主表id',
  `refund_channel` tinyint(4) NULL DEFAULT NULL COMMENT '退款渠道[1-支付宝，2-微信，3-银联，4-汇款]',
  `total_amount` double NULL DEFAULT NULL COMMENT '退款金额',
  `refund_date` datetime(0) NULL DEFAULT NULL COMMENT '退货时间',
  `reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退货原因',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT NULL COMMENT '删除状态',
  PRIMARY KEY (`refund_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

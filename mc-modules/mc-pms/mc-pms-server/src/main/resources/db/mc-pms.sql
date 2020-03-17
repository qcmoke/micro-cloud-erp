/*
 Navicat Premium Data Transfer

 Source Server         : aliyun-mysql
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : 39.106.195.202:3306
 Source Schema         : mc-pms

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 17/03/2020 14:39:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_material
-- ----------------------------
DROP TABLE IF EXISTS `t_material`;
CREATE TABLE `t_material`  (
  `material_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '物料编号',
  `material_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物料名称',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片',
  `unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位',
  `standard` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格',
  `price` double NULL DEFAULT NULL COMMENT '采购单价',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态【0->未删除；1->已删除】',
  `safety_stock` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '安全库存',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`material_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_material
-- ----------------------------
INSERT INTO `t_material` VALUES (1, '骁龙850', 'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1744725657,1391351092&fm=15&gp=0.jpg', '个', '八核', 235, '2020-03-13 04:32:35', '2020-03-17 03:53:02', 0, '2000', NULL);
INSERT INTO `t_material` VALUES (2, '麒麟960', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1584397027830&di=8745cdb03b4b283149bf97921f931c86&imgtype=0&src=http%3A%2F%2Fn1.itc.cn%2Fimg8%2Fwb%2Frecom%2F2016%2F10%2F20%2F147694717679207302.JPEG', '个', '八核', 220, '2020-03-13 04:32:35', '2020-03-17 03:29:14', 0, '2000', NULL);
INSERT INTO `t_material` VALUES (3, '联发科x30', 'https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=793385023,3566315777&fm=26&gp=0.jpg', '个', '八核', 200, '2020-03-13 04:32:35', '2020-03-17 03:29:41', 0, '2000', NULL);
INSERT INTO `t_material` VALUES (4, '英特尔酷睿i5-4210m', 'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1956566071,219264318&fm=26&gp=0.jpg', '个', '四核', 320, '2020-03-13 04:32:35', '2020-03-17 04:21:51', 0, '2000', NULL);
INSERT INTO `t_material` VALUES (5, '京东方 td4310屏', 'http://ww1.sinaimg.cn/large/7b3cb420gy1fkvu4rkg7xj20rs0fkgnv.jpg', '个', '24寸', 560, '2020-03-13 04:32:35', '2020-03-17 03:53:19', 0, '2000', NULL);
INSERT INTO `t_material` VALUES (6, '索尼2000万光学后置摄像头', 'https://www.qcmoke.site/images/logo.jpg', '个', '八核', 102, '2020-03-13 04:32:35', '2020-03-13 04:32:25', 0, '2000', NULL);

-- ----------------------------
-- Table structure for t_material_refund
-- ----------------------------
DROP TABLE IF EXISTS `t_material_refund`;
CREATE TABLE `t_material_refund`  (
  `refund_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '采购退货单主表编号',
  `purchase_order_master_id` bigint(11) NULL DEFAULT NULL COMMENT '采购订单主表编号',
  `refund_channel` tinyint(4) NULL DEFAULT NULL COMMENT '退款渠道[1-支付宝，2-微信，3-银联，4-汇款]',
  `total_amount` double NULL DEFAULT NULL COMMENT '退款总金额',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '退货状态（1:退货中；2:退货成功；3:退货失败）',
  `create_user_id` bigint(20) NOT NULL COMMENT '创建退单的用户id',
  `reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退货原因',
  `out_date` datetime(0) NULL DEFAULT NULL COMMENT '发货日期',
  `finished_time` datetime(0) NULL DEFAULT NULL COMMENT '完成时间',
  `check_status` tinyint(4) NULL DEFAULT 1 COMMENT '审核状态（1:未审核；2:审核不通；3:审核通过）',
  `check_user_id` int(11) NULL DEFAULT NULL COMMENT '最后一次审核的用户id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '退订单创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态【0->未删除；1->已删除】',
  PRIMARY KEY (`refund_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_material_refund
-- ----------------------------
INSERT INTO `t_material_refund` VALUES (5, 30, NULL, NULL, 1, 17, '不要了', '2020-03-17 14:13:18', NULL, 3, 17, '2020-03-17 11:39:22', '2020-03-17 14:13:18', 0);
INSERT INTO `t_material_refund` VALUES (6, 29, NULL, NULL, NULL, 17, '不要了哦', NULL, NULL, 2, 17, '2020-03-17 11:41:34', '2020-03-17 11:48:13', 0);
INSERT INTO `t_material_refund` VALUES (7, 32, NULL, NULL, NULL, 17, '不要了哦', NULL, NULL, 3, 17, '2020-03-17 11:42:23', '2020-03-17 14:31:11', 0);

-- ----------------------------
-- Table structure for t_purchase_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_purchase_order_detail`;
CREATE TABLE `t_purchase_order_detail`  (
  `detail_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '采购订单明细表编号',
  `material_id` bigint(20) NULL DEFAULT NULL COMMENT '物料编号',
  `master_id` bigint(20) NULL DEFAULT NULL COMMENT '采购主表编号',
  `count` double(20, 0) NULL DEFAULT NULL COMMENT '数量',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态【0->未删除；1->已删除】',
  PRIMARY KEY (`detail_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 108 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_purchase_order_detail
-- ----------------------------
INSERT INTO `t_purchase_order_detail` VALUES (84, 4, 24, 2, '2020-03-17 05:11:36', NULL, 0);
INSERT INTO `t_purchase_order_detail` VALUES (85, 1, 25, 3, '2020-03-17 05:17:09', NULL, 0);
INSERT INTO `t_purchase_order_detail` VALUES (86, 1, 25, 3, '2020-03-17 05:30:34', NULL, 0);
INSERT INTO `t_purchase_order_detail` VALUES (87, 2, 25, 1, '2020-03-17 05:30:34', NULL, 0);
INSERT INTO `t_purchase_order_detail` VALUES (88, 3, 25, 1, '2020-03-17 05:30:34', NULL, 0);
INSERT INTO `t_purchase_order_detail` VALUES (89, 4, 25, 1, '2020-03-17 05:30:34', NULL, 0);
INSERT INTO `t_purchase_order_detail` VALUES (90, 2, 26, 2, '2020-03-17 05:36:59', NULL, 0);
INSERT INTO `t_purchase_order_detail` VALUES (91, 2, 26, 2, '2020-03-17 05:37:13', NULL, 0);
INSERT INTO `t_purchase_order_detail` VALUES (92, 3, 26, 1, '2020-03-17 05:37:13', NULL, 0);
INSERT INTO `t_purchase_order_detail` VALUES (93, 4, 26, 1, '2020-03-17 05:37:13', NULL, 0);
INSERT INTO `t_purchase_order_detail` VALUES (94, 2, 27, 1, '2020-03-17 05:39:55', NULL, 0);
INSERT INTO `t_purchase_order_detail` VALUES (95, 3, 27, 2, '2020-03-17 05:39:55', NULL, 0);
INSERT INTO `t_purchase_order_detail` VALUES (96, 1, 28, 1, '2020-03-17 05:40:22', NULL, 0);
INSERT INTO `t_purchase_order_detail` VALUES (97, 3, 28, 4, '2020-03-17 05:40:22', NULL, 0);
INSERT INTO `t_purchase_order_detail` VALUES (98, 1, 29, 2, '2020-03-17 05:47:47', NULL, 0);
INSERT INTO `t_purchase_order_detail` VALUES (99, 2, 29, 1, '2020-03-17 05:47:47', NULL, 0);
INSERT INTO `t_purchase_order_detail` VALUES (100, 3, 29, 1, '2020-03-17 05:47:47', NULL, 0);
INSERT INTO `t_purchase_order_detail` VALUES (101, 1, 30, 3, '2020-03-17 06:39:17', NULL, 0);
INSERT INTO `t_purchase_order_detail` VALUES (102, 2, 30, 1, '2020-03-17 06:39:17', NULL, 0);
INSERT INTO `t_purchase_order_detail` VALUES (103, 1, 31, 1, '2020-03-17 06:40:31', NULL, 1);
INSERT INTO `t_purchase_order_detail` VALUES (104, 3, 31, 1, '2020-03-17 06:40:31', NULL, 1);
INSERT INTO `t_purchase_order_detail` VALUES (105, 1, 32, 1, '2020-03-17 11:41:58', NULL, 0);
INSERT INTO `t_purchase_order_detail` VALUES (106, 2, 32, 1, '2020-03-17 11:41:58', NULL, 0);
INSERT INTO `t_purchase_order_detail` VALUES (107, 3, 32, 1, '2020-03-17 11:41:58', NULL, 0);

-- ----------------------------
-- Table structure for t_purchase_order_master
-- ----------------------------
DROP TABLE IF EXISTS `t_purchase_order_master`;
CREATE TABLE `t_purchase_order_master`  (
  `master_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '采购订单主表id',
  `supplier_id` bigint(20) NULL DEFAULT NULL COMMENT '供应商id',
  `purchase_date` datetime(0) NULL DEFAULT NULL COMMENT '采购时间',
  `pay_type` tinyint(4) NULL DEFAULT NULL COMMENT '支付方式【1->支付宝；2->微信；3->银联； 4->货到付款；】',
  `pay_status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '采购支付状态【1:未支付；2:已支付】',
  `operator_id` bigint(20) NULL DEFAULT NULL COMMENT '操作员id(最后一次修改的操作员)',
  `freight` double NULL DEFAULT NULL COMMENT '运费总金额',
  `freight_pay_status` tinyint(4) NULL DEFAULT NULL COMMENT '运费支付状态【1:未支付；2:已支付】',
  `total_amount` double NULL DEFAULT NULL COMMENT '采购总金额(元)',
  `status` int(10) NOT NULL DEFAULT 0 COMMENT '《入库状态》\r\n0:未提交入库申请；\r\n1:已提交申请未审核，\r\n2:审核不通过\r\n3:审核通过但未入库；\r\n4:审核通过并已入库',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态【0->未删除；1->已删除】',
  PRIMARY KEY (`master_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_purchase_order_master
-- ----------------------------
INSERT INTO `t_purchase_order_master` VALUES (24, 1, '2020-03-17 05:11:36', 1, 2, 17, NULL, 1, 640, 2, NULL, '2020-03-17 05:11:36', '2020-03-17 13:38:07', 0);
INSERT INTO `t_purchase_order_master` VALUES (25, 1, '2020-03-17 05:17:09', 1, 2, 17, 88, 2, 1445, 4, NULL, '2020-03-17 05:17:09', '2020-03-17 07:14:24', 0);
INSERT INTO `t_purchase_order_master` VALUES (26, 1, '2020-03-17 05:36:59', NULL, 2, 17, NULL, 2, 960, 2, NULL, '2020-03-17 05:36:59', '2020-03-17 13:38:10', 0);
INSERT INTO `t_purchase_order_master` VALUES (27, 2, '2020-03-17 05:39:54', NULL, 1, 17, NULL, 1, 620, 1, NULL, '2020-03-17 05:39:54', '2020-03-17 07:12:30', 0);
INSERT INTO `t_purchase_order_master` VALUES (28, 1, '2020-03-17 05:40:22', 1, 2, 17, 22, 2, 1035, 1, NULL, '2020-03-17 05:40:22', '2020-03-17 07:12:32', 0);
INSERT INTO `t_purchase_order_master` VALUES (29, 2, '2020-03-17 05:47:47', 1, 2, 17, 88, 2, 890, 1, NULL, '2020-03-17 05:47:47', '2020-03-17 07:13:48', 0);
INSERT INTO `t_purchase_order_master` VALUES (30, 1, '2020-03-17 06:39:16', 1, 2, 17, 22, 2, 925, 1, '11', '2020-03-17 06:39:16', '2020-03-17 07:13:58', 0);
INSERT INTO `t_purchase_order_master` VALUES (31, 1, '2020-03-17 06:40:31', 1, 2, 17, 21, 2, 435, 0, NULL, '2020-03-17 06:40:31', NULL, 1);
INSERT INTO `t_purchase_order_master` VALUES (32, 1, '2020-03-17 11:41:57', 1, 2, 17, 85, 2, 655, 1, NULL, '2020-03-17 11:41:57', '2020-03-17 11:42:14', 0);

-- ----------------------------
-- Table structure for t_supplier
-- ----------------------------
DROP TABLE IF EXISTS `t_supplier`;
CREATE TABLE `t_supplier`  (
  `supplier_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '供应商编号',
  `supplier_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '供应商地址',
  `bank` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开户银行',
  `bank_account` bigint(20) NULL DEFAULT NULL COMMENT '银行账号',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `link_man` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `link_tel` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人号码',
  `tel_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `postcode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮编',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除状态【0->未删除；1->已删除】',
  PRIMARY KEY (`supplier_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_supplier
-- ----------------------------
INSERT INTO `t_supplier` VALUES (1, '微软', '美国洛杉矶', '中国银行', 5666652245585525, 'microsoft@outlook.com', '比尔盖茨', '11359063321', '11359063321', '556000', '2020-03-13 09:18:57', '2020-03-17 08:18:26', 0);
INSERT INTO `t_supplier` VALUES (2, '索尼', '日本东京都港区港南1-7-1', '建设银行', 5666652245585525, 'suony@outlook.com', '吉田宪一郎', '11359063321', '11359063321', '556000', '2020-03-13 09:21:22', NULL, 0);
INSERT INTO `t_supplier` VALUES (3, '高通', '美国', '中国银行', 5666652245585525, 'microsoft@outlook.com', 'Cristiano Amon', '11359063321', '11359063321', '556000', '2020-03-13 09:18:57', NULL, 0);
INSERT INTO `t_supplier` VALUES (4, '华为', '中国', '中国银行', 5666652245585525, 'microsoft@outlook.com', '任正非', '11359063321', '11359063321', '556000', '2020-03-13 09:18:57', NULL, 0);
INSERT INTO `t_supplier` VALUES (5, '小米', '广东深圳', '建设银行', 5666652245585522, 'leijun@xiaomi.com', '雷军', '18250169888', NULL, '556000', '2020-03-17 08:19:20', NULL, 0);

SET FOREIGN_KEY_CHECKS = 1;

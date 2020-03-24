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

 Date: 24/03/2020 22:43:54
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
  `safety_stock` double NULL DEFAULT NULL COMMENT '安全库存',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`material_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_material
-- ----------------------------
INSERT INTO `t_material` VALUES (1, '骁龙850', 'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1744725657,1391351092&fm=15&gp=0.jpg', '个', '八核', 235, '2020-03-13 04:32:35', '2020-03-17 03:53:02', 0, 2000, NULL);
INSERT INTO `t_material` VALUES (2, '麒麟960', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1584397027830&di=8745cdb03b4b283149bf97921f931c86&imgtype=0&src=http%3A%2F%2Fn1.itc.cn%2Fimg8%2Fwb%2Frecom%2F2016%2F10%2F20%2F147694717679207302.JPEG', '个', '八核', 220, '2020-03-13 04:32:35', '2020-03-17 03:29:14', 0, 2000, NULL);
INSERT INTO `t_material` VALUES (3, '联发科x30', 'https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=793385023,3566315777&fm=26&gp=0.jpg', '个', '八核', 200, '2020-03-13 04:32:35', '2020-03-17 03:29:41', 0, 2000, NULL);
INSERT INTO `t_material` VALUES (4, '英特尔酷睿i5-4210m', 'https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1956566071,219264318&fm=26&gp=0.jpg', '个', '四核', 320, '2020-03-13 04:32:35', '2020-03-17 04:21:51', 0, 2000, NULL);
INSERT INTO `t_material` VALUES (5, '京东方 td4310屏', 'http://ww1.sinaimg.cn/large/7b3cb420gy1fkvu4rkg7xj20rs0fkgnv.jpg', '个', '24寸', 560, '2020-03-13 04:32:35', '2020-03-17 03:53:19', 0, 2000, NULL);
INSERT INTO `t_material` VALUES (6, '索尼PlayStation 4 摄像头（CUH-ZEY2）', 'https://2f.zol-img.com.cn/product/184_320x240/913/ceAcNpDbyhKtU.jpg', '个', '159x28x28mm', 379, '2020-03-13 04:32:35', '2020-03-24 20:07:49', 0, 2000, NULL);
INSERT INTO `t_material` VALUES (7, '麒麟990', 'https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2628240897,1157282899&fm=26&gp=0.jpg', '个', '8核心', 300, '2020-03-24 20:02:41', NULL, 0, 5300, NULL);
INSERT INTO `t_material` VALUES (8, '酷睿i7-9700', 'https://2d.zol-img.com.cn/product/203_280x210/387/ceXq25X5Q2sHY.jpg', '个', '八核14纳米', 2599, '2020-03-24 20:06:16', NULL, 0, 10000, NULL);

-- ----------------------------
-- Table structure for t_material_refund
-- ----------------------------
DROP TABLE IF EXISTS `t_material_refund`;
CREATE TABLE `t_material_refund`  (
  `refund_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '采购退货单主表编号',
  `purchase_order_master_id` bigint(11) NULL DEFAULT NULL COMMENT '采购订单主表编号',
  `refund_channel` tinyint(4) NULL DEFAULT NULL COMMENT '退款渠道支付方式【1->支付宝；2->微信；3->银联】',
  `total_amount` double NULL DEFAULT NULL COMMENT '退款总金额',
  `reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退货原因',
  `refund_date` datetime(0) NULL DEFAULT NULL COMMENT '退货时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '退订单创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态【0->未删除；1->已删除】',
  PRIMARY KEY (`refund_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_material_refund
-- ----------------------------
INSERT INTO `t_material_refund` VALUES (9, 34, NULL, NULL, '不要了', '2020-03-24 16:50:07', '2020-03-18 11:25:52', '2020-03-24 16:50:12', 1);
INSERT INTO `t_material_refund` VALUES (10, 79, NULL, NULL, '破恐龙', NULL, '2020-03-24 15:39:56', '2020-03-24 16:51:55', 1);
INSERT INTO `t_material_refund` VALUES (11, 80, NULL, NULL, 'qqq', NULL, '2020-03-24 15:59:24', NULL, 1);
INSERT INTO `t_material_refund` VALUES (12, 81, NULL, NULL, 'qq', NULL, '2020-03-24 16:21:16', NULL, 1);
INSERT INTO `t_material_refund` VALUES (13, 83, NULL, NULL, 'yyy', NULL, '2020-03-24 16:33:42', NULL, 1);
INSERT INTO `t_material_refund` VALUES (14, 79, NULL, NULL, 'ooo', '2020-03-24 18:35:13', '2020-03-24 18:35:13', NULL, 1);
INSERT INTO `t_material_refund` VALUES (15, 79, NULL, NULL, 'ooo', '2020-03-24 18:35:16', '2020-03-24 18:35:16', NULL, 1);
INSERT INTO `t_material_refund` VALUES (16, 79, NULL, NULL, 'ooo', '2020-03-24 18:35:23', '2020-03-24 18:35:23', NULL, 1);
INSERT INTO `t_material_refund` VALUES (17, 82, NULL, NULL, 'pp', '2020-03-24 18:35:36', '2020-03-24 18:35:36', NULL, 1);
INSERT INTO `t_material_refund` VALUES (18, 84, NULL, NULL, '', '2020-03-24 18:36:15', '2020-03-24 18:36:15', NULL, 1);
INSERT INTO `t_material_refund` VALUES (19, 79, 1, 2640, '', '2020-03-24 19:36:57', '2020-03-24 19:36:57', NULL, 1);
INSERT INTO `t_material_refund` VALUES (20, 79, 1, 2640, '', '2020-03-24 19:37:38', '2020-03-24 19:37:38', NULL, 1);
INSERT INTO `t_material_refund` VALUES (21, 80, 1, 960, '', '2020-03-24 19:46:20', '2020-03-24 19:46:20', NULL, 1);
INSERT INTO `t_material_refund` VALUES (22, 81, NULL, 2240, '不要了', '2020-03-24 19:47:07', '2020-03-24 19:47:07', NULL, 1);
INSERT INTO `t_material_refund` VALUES (23, 79, 3, 2640, '不要了啊', '2020-03-24 19:47:48', '2020-03-24 19:47:48', NULL, 0);
INSERT INTO `t_material_refund` VALUES (24, 81, NULL, 2240, '不要了啊', '2020-03-24 19:48:00', '2020-03-24 19:48:00', NULL, 1);
INSERT INTO `t_material_refund` VALUES (25, 82, 1, 1120, '不要了啊', '2020-03-24 19:48:11', '2020-03-24 19:48:11', NULL, 0);
INSERT INTO `t_material_refund` VALUES (26, 80, 1, 960, '不要了啊', '2020-03-24 19:48:21', '2020-03-24 19:48:21', NULL, 0);
INSERT INTO `t_material_refund` VALUES (27, 80, 1, 960, '不要了啊', '2020-03-24 19:48:36', '2020-03-24 19:48:36', NULL, 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 286 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_purchase_order_detail
-- ----------------------------
INSERT INTO `t_purchase_order_detail` VALUES (275, 5, 79, 3, '2020-03-24 15:39:43', '2020-03-24 15:39:43', 0);
INSERT INTO `t_purchase_order_detail` VALUES (276, 4, 79, 3, '2020-03-24 15:39:43', '2020-03-24 15:39:43', 0);
INSERT INTO `t_purchase_order_detail` VALUES (277, 4, 80, 3, '2020-03-24 15:59:03', '2020-03-24 15:59:03', 1);
INSERT INTO `t_purchase_order_detail` VALUES (278, 5, 81, 4, '2020-03-24 16:21:09', '2020-03-24 16:21:09', 1);
INSERT INTO `t_purchase_order_detail` VALUES (279, 5, 82, 2, '2020-03-24 16:29:46', '2020-03-24 16:29:46', 0);
INSERT INTO `t_purchase_order_detail` VALUES (280, 6, 83, 2, '2020-03-24 16:33:26', '2020-03-24 16:33:26', 1);
INSERT INTO `t_purchase_order_detail` VALUES (281, 4, 84, 1, '2020-03-24 18:36:09', '2020-03-24 18:36:09', 0);
INSERT INTO `t_purchase_order_detail` VALUES (282, 4, 80, 3, '2020-03-24 19:39:36', '2020-03-24 19:39:36', 1);
INSERT INTO `t_purchase_order_detail` VALUES (283, 4, 80, 3, '2020-03-24 19:39:44', '2020-03-24 19:39:44', 0);
INSERT INTO `t_purchase_order_detail` VALUES (284, 5, 81, 4, '2020-03-24 19:53:34', '2020-03-24 19:53:34', 0);
INSERT INTO `t_purchase_order_detail` VALUES (285, 6, 83, 2, '2020-03-24 19:54:42', '2020-03-24 19:54:42', 0);

-- ----------------------------
-- Table structure for t_purchase_order_master
-- ----------------------------
DROP TABLE IF EXISTS `t_purchase_order_master`;
CREATE TABLE `t_purchase_order_master`  (
  `master_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '采购订单主表id',
  `supplier_id` bigint(20) NULL DEFAULT NULL COMMENT '供应商id',
  `purchase_date` datetime(0) NULL DEFAULT NULL COMMENT '采购时间',
  `pay_type` tinyint(4) NULL DEFAULT NULL COMMENT '支付方式【1->支付宝；2->微信；3->银联】',
  `pay_status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '支付状态(包含运费)【1:未支付；2:已支付】',
  `total_amount` double NULL DEFAULT NULL COMMENT '采购总金额(元)',
  `freight` double NULL DEFAULT NULL COMMENT '运费金额',
  `operator_id` bigint(20) NULL DEFAULT NULL COMMENT '操作员id(最后一次修改的操作员)',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '《入库状态》\r\n1:未提交入库申请\r\n2:已提交申请但未审核\r\n3:审核不通过\r\n4:审核通过\r\n5.未入库\r\n6.已入库',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_status` tinyint(4) NULL DEFAULT 0 COMMENT '删除状态【0->未删除；1->已删除】',
  `transfer_stock_status` tinyint(4) NULL DEFAULT 1 COMMENT '移交库存状态(1:未移交；2:已移交申请；3:移交失败；4:已完成移交；)',
  PRIMARY KEY (`master_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 85 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_purchase_order_master
-- ----------------------------
INSERT INTO `t_purchase_order_master` VALUES (79, 2, '2020-03-24 15:39:43', 3, 2, 2640, 23, 17, 1, '', '2020-03-24 15:39:43', NULL, 0, 1);
INSERT INTO `t_purchase_order_master` VALUES (80, 1, '2020-03-24 15:59:03', 2, 2, 960, 22, 17, 1, '', '2020-03-24 15:59:03', '2020-03-24 19:39:43', 0, 1);
INSERT INTO `t_purchase_order_master` VALUES (81, 2, '2020-03-24 16:21:08', 1, 2, 2240, 22, 17, 4, '', '2020-03-24 16:21:08', '2020-03-24 19:57:18', 0, 1);
INSERT INTO `t_purchase_order_master` VALUES (82, 2, '2020-03-24 16:29:46', 1, 2, 1120, 25, 17, 1, '', '2020-03-24 16:29:46', NULL, 0, 1);
INSERT INTO `t_purchase_order_master` VALUES (83, 2, '2020-03-24 16:33:26', 3, 2, 204, 52, 17, 2, '', '2020-03-24 16:33:26', '2020-03-24 19:54:47', 0, 1);
INSERT INTO `t_purchase_order_master` VALUES (84, 1, '2020-03-24 18:36:08', NULL, 1, 320, NULL, 17, 1, '', '2020-03-24 18:36:08', NULL, 0, 1);

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
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_supplier
-- ----------------------------
INSERT INTO `t_supplier` VALUES (1, '微软', '美国洛杉矶', '中国银行', 5666652245585525, 'microsoft@outlook.com', '比尔盖茨', '11359063321', '11359063321', '556000', '2020-03-23 18:55:41', '2020-03-24 20:08:23', 0);
INSERT INTO `t_supplier` VALUES (2, '索尼', '日本东京都港区港南1-7-1', '建设银行', 5666652245585525, 'suony@outlook.com', '吉田宪一郎', '11359063321', '11359063321', '556000', '2020-03-23 18:59:03', '2020-03-24 20:00:45', 0);
INSERT INTO `t_supplier` VALUES (3, '高通', '美国', '中国银行', 5666652245585525, 'microsoft@outlook.com', 'Cristiano Amon', '11359063321', '11359063321', '556000', '2020-03-23 18:55:53', NULL, 0);
INSERT INTO `t_supplier` VALUES (4, '华为', '中国', '中国银行', 5666652245585525, 'microsoft@outlook.com', '任正非', '11359063321', '11359063321', '556000', '2020-03-23 18:55:59', '2020-03-24 20:03:04', 0);
INSERT INTO `t_supplier` VALUES (5, '小米', '广东深圳', '建设银行', 5666652245585522, 'leijun@xiaomi.com', '雷军', '18250169888', NULL, '556000', '2020-03-23 18:58:43', NULL, 0);
INSERT INTO `t_supplier` VALUES (6, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2020-03-23 16:39:24', NULL, 1);
INSERT INTO `t_supplier` VALUES (9, '长虹电子', '深圳南山', '建设银行', 4465498412347, 'changhong@changhong.com', 'changhong老板', '18250169860', '18250169860', '556000', NULL, NULL, 1);

-- ----------------------------
-- Table structure for t_supplier_material
-- ----------------------------
DROP TABLE IF EXISTS `t_supplier_material`;
CREATE TABLE `t_supplier_material`  (
  `supplier_material_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `supplier_id` bigint(20) NOT NULL,
  `material_id` bigint(20) NOT NULL,
  `delete_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除状态【0->未删除；1->已删除】',
  PRIMARY KEY (`supplier_material_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_supplier_material
-- ----------------------------
INSERT INTO `t_supplier_material` VALUES (41, 3, 1, 0);
INSERT INTO `t_supplier_material` VALUES (43, 5, 3, 0);
INSERT INTO `t_supplier_material` VALUES (44, 5, 2, 0);
INSERT INTO `t_supplier_material` VALUES (45, 5, 1, 0);
INSERT INTO `t_supplier_material` VALUES (46, 5, 5, 0);
INSERT INTO `t_supplier_material` VALUES (47, 5, 4, 0);
INSERT INTO `t_supplier_material` VALUES (48, 5, 6, 0);
INSERT INTO `t_supplier_material` VALUES (53, 2, 6, 0);
INSERT INTO `t_supplier_material` VALUES (54, 4, 2, 0);
INSERT INTO `t_supplier_material` VALUES (55, 4, 7, 0);
INSERT INTO `t_supplier_material` VALUES (56, 1, 4, 0);
INSERT INTO `t_supplier_material` VALUES (57, 1, 8, 0);

SET FOREIGN_KEY_CHECKS = 1;

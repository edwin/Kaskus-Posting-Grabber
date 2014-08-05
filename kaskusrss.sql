/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50144
Source Host           : localhost:3306
Source Database       : kaskusrss

Target Server Type    : MYSQL
Target Server Version : 50144
File Encoding         : 65001

Date: 2014-08-05 22:44:29
*/

SET FOREIGN_KEY_CHECKS=0;


-- ----------------------------
-- Table structure for `threads`
-- ----------------------------
DROP TABLE IF EXISTS `threads`;
CREATE TABLE `threads` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `thread_id` varchar(48) DEFAULT NULL,
  `thread_name` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of threads
-- ----------------------------

-- ----------------------------
-- Table structure for `posts`
-- ----------------------------
DROP TABLE IF EXISTS `posts`;
CREATE TABLE `posts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `post_id` varchar(48) DEFAULT NULL,
  `post_content` text,
  `post_author` varchar(100) DEFAULT NULL,
  `post_date` datetime DEFAULT NULL,
  `thread_id` varchar(48) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `postid` (`post_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1674 DEFAULT CHARSET=latin1;


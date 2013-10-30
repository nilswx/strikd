<?php
$appId = 675897809090251;
$appNs = 'strik-fb';
$objectType = $_REQUEST['type'];
$objectData = $_REQUEST['data'];

// TODO: deal with this stuff, format image links etc
?>

<html>
  <head prefix="og: http://ogp.me/ns#">
    <meta property="fb:app_id" content="<?= $appId ?>">
      <meta property="og:type" content="<?= $appNs ?>:<?= $objectType ?>">
      <meta property="og:title" content="<?php echo strip_tags($_REQUEST['og:title']);?>">
      <meta property="og:image" content="<?php echo strip_tags($_REQUEST['og:image']);?>">
      <meta property="og:description" content="<?php echo strip_tags($_REQUEST['og:description']);?>">
      <title><?= $objectType ?></title>
  </head>
    <body>
      <?php echo strip_tags($_REQUEST['body']);?>
    </body>
</html>
package com.grucis.dev.service;

import java.awt.image.BufferedImage;

import com.grucis.dev.exporter.BitmapExporter;
import com.grucis.dev.exporter.SpriteExporter;
import com.grucis.dev.io.ModelLoader;
import com.grucis.dev.mapper.export.AnimationSpriteMapper;
import com.grucis.dev.mapper.export.OffsetBitmapMapper;
import com.grucis.dev.model.export.bitmap.BitmapIndex;
import com.grucis.dev.model.export.bitmap.OffsetBitmap;
import com.grucis.dev.model.export.sprite.animation.AnimationSprite;
import com.grucis.dev.model.export.sprite.animation.AnimationSpriteIndex;
import com.grucis.dev.model.output.AnimationMap;
import com.grucis.dev.model.output.OffsetImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExportModelService {

  private static final Logger LOG = LoggerFactory.getLogger(ExportModelService.class);

  @Autowired
  private OutputModelService outputModelService;
  @Autowired
  private OffsetBitmapMapper offsetBitmapMapper;
  @Autowired
  private AnimationSpriteMapper animationSpriteMapper;
  @Autowired
  private BitmapExporter bitmapExporter;
  @Autowired
  private SpriteExporter spriteExporterr;
  @Autowired
  private ModelLoader modelLoader;

  public OffsetBitmap getOffsetBitmap(int id, boolean refresh) {
    OffsetBitmap ret;

    if(refresh || (ret = modelLoader.loadOffsetBitmap(id)) == null) {
      OffsetImage image = outputModelService.getOffsetImage(id);
      ret = offsetBitmapMapper.map(image);
      try {
        bitmapExporter.export(ret);
      } catch(Exception e) {
        LOG.error("Cannot export OffsetBitmap #{}", id);
      }
    }

    return ret;
  }

  public BufferedImage getBitmapImage(int id, boolean refresh) {
    BufferedImage ret;

    if(refresh || (ret = modelLoader.loadBitmapImage(id)) == null) {
      ret = getOffsetBitmap(id, true).getImage();
    }

    return ret;
  }

  public BitmapIndex getBitmapIndex(int id, boolean refresh) {
    BitmapIndex ret;

    if(refresh || (ret = modelLoader.loadBitmapIndex(id)) == null) {
      ret = getOffsetBitmap(id, true).getIndex();
    }

    return ret;
  }

  public AnimationSprite getAnimationSprite(int id, boolean refresh) {
    AnimationSprite ret;

    if(refresh || (ret = modelLoader.loadAnimationSprite(id)) == null) {
      AnimationMap animation = outputModelService.getAnimationMap(id);
      ret = animationSpriteMapper.map(animation);
      try {
        spriteExporterr.export(ret);
      } catch(Exception e) {
        LOG.error("Cannot export AnimationSprite #{}", id);
      }
    }

    return ret;
  }

  public BufferedImage getAnimationSpriteImage(int id, boolean refresh) {
    BufferedImage ret;

    if(refresh || (ret = modelLoader.loadAnimationSpriteImage(id)) == null) {
      ret = getAnimationSprite(id, true).getImage();
    }

    return ret;
  }

  public AnimationSpriteIndex getAnimationSpriteIndex(int id, boolean refresh) {
    AnimationSpriteIndex ret;

    if(refresh || (ret = modelLoader.loadAnimationSpriteIndex(id)) == null) {
      ret = getAnimationSprite(id, true).getIndex();
    }

    return ret;
  }
}
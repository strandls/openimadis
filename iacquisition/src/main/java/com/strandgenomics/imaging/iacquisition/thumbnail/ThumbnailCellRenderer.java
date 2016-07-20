/**
 * openImaDis - Open Image Discovery: Image Life Cycle Management Software
 * Copyright (C) 2011-2016  Strand Life Sciences
 *   
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.strandgenomics.imaging.iacquisition.thumbnail;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.strandgenomics.imaging.icore.IRecord;

@SuppressWarnings("serial")
public class ThumbnailCellRenderer extends DefaultTableCellRenderer {

//	private static final String CONFIG_ID = "imaging.viz:ThumbnailView";

    private int thumbnailHeight;
    private int thumbnailWidth;

//	Configuration config;

//    private LinkedHashMap<IRecord, BufferedImage> imageCache;
    /*
     * Restricting cache to a few records
     */
//    private static final int CACHE_SIZE = 100;

    public ThumbnailCellRenderer() {
        super();
//		config = ConfigurationManager.getConfiguration(CONFIG_ID);
//		thumbnailWidth = (Integer) config.getValue("width");
//		thumbnailHeight = (Integer) config.getValue("height");
        thumbnailWidth = 120;
        thumbnailHeight = 120;

//        imageCache = new LinkedHashMap<IRecord, BufferedImage>() {
//            @Override
//            protected boolean removeEldestEntry(Map.Entry<IRecord, BufferedImage> entry) {
//                return size() > CACHE_SIZE;
//            }
//        };
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col) {

        if (value != null)
        {
            IRecord record = (IRecord) value;
            return new MyImagePreviewPanel(record, "", "", isSelected, hasFocus);
        }
        return new DummyImagePanel();
    }

    class DummyImagePanel extends JComponent {

    }


    class MyImagePreviewPanel extends JComponent 
    {
        private IRecord record;
        private boolean isSelected;

        MyImagePreviewPanel(IRecord record, String title, String desc,
                boolean isSelected, boolean hasFocus) {
            this.record = record;

            this.isSelected = isSelected;
            setMinimumSize(new Dimension(thumbnailWidth + 10,
                    thumbnailHeight + 10));
        }

        public void paint(java.awt.Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            BufferedImage image = getImage();
            if (isSelected) {
                g2.setColor(new Color(204, 204, 255));
                g2.fillRect(0, 0, this.getWidth(), this.getHeight());
            }
            if (image != null) {
                int newHeight = thumbnailHeight - 15;
                int newWidth = thumbnailWidth - 15;
                int hgap = (this.getHeight() - newHeight) / 2;
                int wgap = (this.getWidth() - newWidth) / 2;
                g2.drawImage(image, wgap, hgap, newWidth + wgap, newHeight
                        + hgap, 0, 0, image.getWidth(), image.getHeight(), null);
            }
        };

        public BufferedImage getImage(){
//            synchronized (imageCache) {
//                if(!imageCache.containsKey(record)) {
//                    new ThumbnailRetriever(record).run();
//                }
//                return imageCache.get(record);
//            }
        	return record.getThumbnail();
        }

    }

//    private class ThumbnailRetriever extends SwingWorker {
//
//        private IRecord record;
//
//        private ThumbnailRetriever(IRecord record) {
//            this.record = record;
//        }
//        @Override
//        protected Object doInBackground() throws Exception 
//        {
//            BufferedImage image = record.getThumbnail();
//            synchronized (imageCache) 
//            {
//                imageCache.put(record, image);
//            }
//            firePropertyChange("Thumbnail", null, null);   //in the hope that table listens and updates itself
//            return null;
//        }
//
//    }

	public void clearCacheForRecord(IRecord record)
	{
//		imageCache.remove(record);
		revalidate();
	}
}

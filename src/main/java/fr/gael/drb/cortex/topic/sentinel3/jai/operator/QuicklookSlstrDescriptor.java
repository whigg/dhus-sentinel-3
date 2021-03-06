/*
 * Data HUb Service (DHuS) - For Space data distribution.
 * Copyright (C) 2013,2014,2015,2016 European Space Agency (ESA)
 * Copyright (C) 2013,2014,2015,2016 GAEL Systems
 * Copyright (C) 2013,2014,2015,2016 Serco Spa
 *
 * This file is part of DHuS software sources.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package fr.gael.drb.cortex.topic.sentinel3.jai.operator;

import fr.gael.drb.cortex.topic.sentinel3.jai.operator.Common.PixelCorrection;

import javax.media.jai.JAI;
import javax.media.jai.OperationDescriptorImpl;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.registry.RenderedRegistryMode;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;

/**
 * Describes the SLSTR Quicklook generator operator.
 */
public class QuicklookSlstrDescriptor extends OperationDescriptorImpl
{
   private static final long serialVersionUID = -8759205585877624076L;
   /**
    * The "QuicklookSlstr" operation name.
    */
   public final static String OPERATION_NAME = "QuicklookSlstr";

   /**
    * Modes supported by this operator.
    */
   private static String[] supportedModes = { "rendered" };

   /**
    * The resource strings that provide the general documentation and
    * specify the parameter list for the "SLSTR" operation.
    */
   protected static String[][] resources =
           {
                   { "GlobalName", OPERATION_NAME },
                   { "LocalName", OPERATION_NAME },
                   { "Vendor", "fr.gael.drb.cortex.topic.sentinel3.jai.operator" },
                   { "Description", "Performs the rendering of S3 SLSTR L1 dataset." },
                   { "DocURL", "http://www.gael.fr/drb" },
                   { "Version", "1.0" },
                   { "arg0Desc", "per band pixels correction"},
                   { "arg1Desc", "detector indexes"},
                   { "arg2Desc", "solar zenith angle"},
                   { "arg3Desc", "solar flux s5"},
                   { "arg4Desc", "solar flux s3"},
                   { "arg5Desc", "solar flux s1"},
           };

   /**
    * The parameter names for the operation..
    */
   private static String[] paramNames = {"pixels_correction", "detectors", "sza", "solar_flux_s5",
           "solar_flux_s3", "solar_flux_s1" };

   /**
    * The parameter class types for the operation.
    */
   private static Class<?>[] paramClasses = { PixelCorrection[].class, short[][].class, double[][].class,
           double[].class, double[].class, double[].class};

   public QuicklookSlstrDescriptor()
   {
      super(resources, supportedModes, 3, paramNames, paramClasses,
              null, null);
   }

   /**
    * Create the Render Operator to compute SLSTR quicklook.
    *
    * <p>Creates a <code>ParameterBlockJAI</code> from all
    * supplied arguments except <code>hints</code> and invokes
    * {@link JAI#create(String,ParameterBlock,RenderingHints)}.
    *
    * @see JAI
    * @see ParameterBlockJAI
    * @see RenderedOp
    *
    * @return The <code>RenderedOp</code> destination.
    * @throws IllegalArgumentException if sources is null.
    * @throws IllegalArgumentException if a source is null.
    */
   public static RenderedOp create(PixelCorrection[]pixels_correction, short[][] detectors, double[][]sza,
                                   float[][] solar_flux_s5, float[][] solar_flux_s3, float[][] solar_flux_s1,
                                   RenderingHints hints, RenderedImage... sources)
   {
      ParameterBlockJAI pb = new ParameterBlockJAI(OPERATION_NAME, RenderedRegistryMode.MODE_NAME);

      int numSources = sources.length;
      // Check on the source number
      if (numSources <= 0)
      {
         throw new IllegalArgumentException("No resources are present");
      }

      // Setting of all the sources
      for (int index = 0; index < numSources; index++)
      {
         RenderedImage source = sources[index];
         if (source == null)
         {
            throw new IllegalArgumentException("This resource is null");
         }
         pb.setSource(source, index);
      }
      /*To Be remove */
      pb.setParameter(paramNames[0], pixels_correction);
      pb.setParameter(paramNames[1], detectors);
      pb.setParameter(paramNames[2], sza);
      pb.setParameter(paramNames[3], solar_flux_s5);
      pb.setParameter(paramNames[4], solar_flux_s3);
      pb.setParameter(paramNames[5], solar_flux_s1);

      return JAI.create(OPERATION_NAME, pb, hints);
   } //create
}

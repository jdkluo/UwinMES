package com.redlichee.uwinmes.widget;
 


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.redlichee.uwinmes.R;

/**
 * 
 * Create custom Dialog windows for your application
 * Custom dialogs rely on custom layouts wich allow you to 
 * create and use your own look & feel.
 * 
 * Under GPL v3 : http://www.gnu.org/licenses/gpl-3.0.html
 * 
 * @author antoine vianey
 *
 */
public class CustomDialog extends Dialog {
	
    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }
 
    public CustomDialog(Context context) {
        super(context);
    }
 
    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder {
		private String TAG = "CustomDialog";
		
        private Context context;
        private String title;
        private String message;
        private boolean mCancelable;
        private String positiveButtonText;
        private String negativeButtonText;
        private String neutralButtonText;
        private View contentView;
 
        private OnClickListener
                        positiveButtonClickListener,
                        negativeButtonClickListener,
                        neutralButtonClickListener;


        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Set the Dialog message from String
         * @param message
         * @return
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         * @param message
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Sets whether the dialog is cancelable or not.  Default is true.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setCancelable(boolean cancelable) {
            this.mCancelable = cancelable;
            return this;
        }

        /**
         * Set the Dialog Cancelable from resource
         * @return
         */
        public Builder setCancelable(){
            return this;
        }

        /**
         * Set the Dialog title from String
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Set a custom content view for the Dialog.
         * If a message is set, the contentView is not
         * added to the Dialog...
         * @param v
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * Set the positive button text and it's listener
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(String positiveButtonText,
                OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * Set the negative button resource and it's listener
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(int negativeButtonText,
                OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         * Set the negative button text and it's listener
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(String negativeButtonText,
                OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         * Set the neutral button resource and it's listener
         * @param neutralButtonText
         * @param listener
         * @return
         */
        public Builder setNeutralButton(int neutralButtonText,
        		OnClickListener listener) {
        	this.neutralButtonText = (String) context
        			.getText(neutralButtonText);
        	this.neutralButtonClickListener = listener;
        	return this;
        }

        /**
         * Set the neutral button text and it's listener
         * @param neutralButtonText
         * @param listener
         * @return
         */
        public Builder setNeutralButton(String neutralButtonText,
        		OnClickListener listener) {
        	this.neutralButtonText = neutralButtonText;
        	this.neutralButtonClickListener = listener;
        	return this;
        }
 
        /**
         * Create the custom dialog
         */
        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomDialog dialog = new CustomDialog(context, R.style.CustomDialog);
            View layout = inflater.inflate(R.layout.dialog_custom, null);
            dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            ((TextView) layout.findViewById(R.id.title_textView)).setText(title);
            dialog.setCancelable(this.mCancelable);
            if (this.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positive_button)).setText(positiveButtonText);
                ((Button) layout.findViewById(R.id.positive_button))
	                    .setOnClickListener(new View.OnClickListener() {
	                        public void onClick(View v) {
	                        	 dialog.dismiss();
	                        	  if (positiveButtonClickListener != null) {
	                        		  positiveButtonClickListener.onClick(
	                            		dialog, 
	                                    DialogInterface.BUTTON_POSITIVE);
	                        	  }	                        	 
	                        }
	              });
                
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.buttonLayout_confirm).setVisibility(
                        View.GONE);
            }
            // set the neutral button
            if (negativeButtonText != null) {
            	((Button) layout.findViewById(R.id.negative_button)).setText(negativeButtonText);
            	((Button) layout.findViewById(R.id.negative_button))
            	.setOnClickListener(new View.OnClickListener() {
            		public void onClick(View v) {   
            			dialog.dismiss();
            			if (negativeButtonClickListener != null) {
            				negativeButtonClickListener.onClick(dialog, 
            						DialogInterface.BUTTON_NEGATIVE);
            			}
            		}
            	});
            	
            	
            } else {
            	// if no confirm button just set the visibility to GONE
            	layout.findViewById(R.id.buttonLayout_cancel).setVisibility(
            			View.GONE);
            }
            // set the neutral button
            if (neutralButtonText != null) {
            	((Button) layout.findViewById(R.id.neutral_button)).setText(neutralButtonText);
            	((Button) layout.findViewById(R.id.neutral_button))
            	.setOnClickListener(new View.OnClickListener() {
            		public void onClick(View v) {   
            			dialog.dismiss();
            			if (neutralButtonClickListener != null) {
            				neutralButtonClickListener.onClick(dialog, 
            						DialogInterface.BUTTON_NEUTRAL);
            			}
            		}
            	});
            	
            	
            } else {
            	// if no confirm button just set the visibility to GONE
            	layout.findViewById(R.id.buttonLayout_neutral).setVisibility(View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negative_button)).setText(negativeButtonText);
                ((Button) layout.findViewById(R.id.negative_button))
                	.setOnClickListener(new View.OnClickListener() {
                       public void onClick(View v) {   
                    	    dialog.dismiss();
                           if (negativeButtonClickListener != null) {
                        	   negativeButtonClickListener.onClick(dialog, 
                                            DialogInterface.BUTTON_NEGATIVE);
                          	}
                        }
                 });
                
                
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.buttonLayout_cancel).setVisibility(
                        View.GONE);
            }
            // set the content message
            if (message != null) {
                ((TextView) layout.findViewById(R.id.content_textView)).setText(message);
            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.content_layout)).removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content_layout)).addView(contentView, 
                                new LayoutParams(LayoutParams.WRAP_CONTENT, 
                                        LayoutParams.WRAP_CONTENT));
            }
            dialog.setContentView(layout);
            return dialog;
        }
        
        public void show(){
        	create().show();
        }
 
    }
 
}
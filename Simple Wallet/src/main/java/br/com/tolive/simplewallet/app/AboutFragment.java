package br.com.tolive.simplewallet.app;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import br.com.tolive.simplewallet.constants.Constantes;
import br.com.tolive.simplewallet.utils.IabHelper;
import br.com.tolive.simplewallet.utils.IabResult;
import br.com.tolive.simplewallet.utils.Inventory;
import br.com.tolive.simplewallet.utils.Purchase;
import br.com.tolive.simplewallet.views.CustomTextView;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class AboutFragment extends Fragment {
    private static final int RC_REQUEST = 0;
    // The helper object
    private IabHelper mHelper;
    CustomTextView mTextPrice;
    CustomTextView mTextRemoveAd;
    SharedPreferences sharedPreferences;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        sharedPreferences = getActivity().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        mTextPrice = (CustomTextView) view.findViewById(R.id.fragment_about_text_remove_ad_price);
        mTextRemoveAd = (CustomTextView) view.findViewById(R.id.fragment_about_text_remove_ad);
        boolean removeAd = sharedPreferences.getBoolean(Constantes.SP_KEY_REMOVE_AD, Constantes.SP_REMOVE_AD_DEFAULT);
        if(removeAd) {
            mTextPrice.setText(R.string.fragment_about_text_remove_ad_price_removed);
            mTextRemoveAd.setText(R.string.fragment_about_text_remove_ad_removed);
        }

        RelativeLayout containerToLiveHealthy = (RelativeLayout) view.findViewById(R.id.fragment_about_tolivehealthy);
        RelativeLayout containerGSP = (RelativeLayout) view.findViewById(R.id.fragment_about_gastossimplespro);
        RelativeLayout containerFbFanpage = (RelativeLayout) view.findViewById(R.id.fragment_about_fb_fanpage);
        final RelativeLayout containerRemoveAd = (RelativeLayout) view.findViewById(R.id.fragment_about_container_remove_ad);


        // Create the helper, passing it our context and the public key to
        // verify signatures with
        mHelper = new IabHelper(getActivity(), Constantes.BILLING_base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set
        // this to false).
        mHelper.enableDebugLogging(false);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                //Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    //alert("Problem setting up in-app billing: " + result);
                    Toast.makeText(getActivity(), "Problem setting up in-app billing: " + result, Toast.LENGTH_LONG).show();
                    return;
                }

                // Hooray, IAB is fully set up. Now, let's get an inventory of
                // stuff we own.
                //Log.d(TAG, "Setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });

        containerToLiveHealthy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = Constantes.PACKAGE_TOLIVE_HEALTHY;
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        containerGSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = Constantes.PACKAGE_GASTOS_SIMPLES_PRO;
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        containerFbFanpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(getOpenFacebookIntent(getActivity()));
            }
        });

        containerRemoveAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean removeAd = sharedPreferences.getBoolean(Constantes.SP_KEY_REMOVE_AD, Constantes.SP_REMOVE_AD_DEFAULT);
                if(!removeAd) {
                    String payload = "";

    //				mHelper.launchPurchaseFlow(BuyDietsActivity.this, sku, RC_REQUEST,
    //						mPurchaseFinishedListener, payload);

                    mHelper.launchPurchaseFlow(getActivity(), Constantes.SKU, RC_REQUEST,
                            new IabHelper.OnIabPurchaseFinishedListener() {
                                public void onIabPurchaseFinished(IabResult result, Purchase purchase) {

//						Log.d(TAG, "Purchase finished: " + result + ", purchase: "
//							+ purchase);
                                    if (result.isFailure()) {
                                        //alert("Error purchasing: " + result);
                                        return;
                                    }
                                    if (!verifyDeveloperPayload(purchase)) {
                                        //alert("Error purchasing. Authenticity verification failed.");
                                        return;
                                    }

                                    //Log.d(TAG, "Purchase successful.");

                                    if (purchase.getSku().equals(Constantes.SKU)) {
                                        // bought the premium upgrade!
                                        //alert("Obrigado pela compra! Siga a dieta e atinja seu objetivo :)");
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean(Constantes.SP_KEY_REMOVE_AD, true);
                                        editor.commit();
                                        ((MenuActivity) getActivity()).removeAd();
                                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.fragment_about_toast_remove_ad), Toast.LENGTH_SHORT).show();
                                        mTextPrice.setText(R.string.fragment_about_text_remove_ad_price_removed);
                                        mTextRemoveAd.setText(R.string.fragment_about_text_remove_ad_removed);
                                    }
                                }
                            }, payload
                    );
                }
            }
        });

        return view;
    }

    // Listener that's called when we finish querying the items and
    // subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {
            //Log.d(TAG, "Query inventory finished.");
            if (result.isFailure()) {
                //alert("Failed to query inventory: " + result);
                return;
            }

            //Log.d(TAG, "Query inventory was successful.");

			/*
			 * Check for items we own. Notice that for each purchase, we check
			 * the developer payload to see if it's correct! See
			 * verifyDeveloperPayload().
			 */

            // Do we have the DIET_1?
                Purchase removeAdPurchase = inventory.getPurchase(Constantes.SKU);
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constantes.SHARED_PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if(removeAdPurchase != null && verifyDeveloperPayload(removeAdPurchase)){
                    editor.putBoolean(Constantes.SP_KEY_REMOVE_AD, true);
                    mTextPrice.setText(R.string.fragment_about_text_remove_ad_price_removed);
                    mTextRemoveAd.setText(R.string.fragment_about_text_remove_ad_removed);
                } else{
                    editor.putBoolean(Constantes.SP_KEY_REMOVE_AD, false);
                    mTextPrice.setText(R.string.fragment_about_text_remove_ad_price);
                    mTextRemoveAd.setText(R.string.fragment_about_text_remove_ad);
                }
                editor.commit();


            //Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + ","
        //		+ data);

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            //Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        //String payload = p.getDeveloperPayload();

		/*
		 * TODO: verify that the developer payload of the purchase is correct.
		 * It will be the same one that you sent when initiating the purchase.
		 *
		 * WARNING: Locally generating a random string when starting a purchase
		 * and verifying it here might seem like a good approach, but this will
		 * fail in the case where the user purchases an item on one device and
		 * then uses your app on a different device, because on the other device
		 * you will not have access to the random string you originally
		 * generated.
		 *
		 * So a good developer payload has these characteristics:
		 *
		 * 1. If two different users purchase an item, the payload is different
		 * between them, so that one user's purchase can't be replayed to
		 * another user.
		 *
		 * 2. The payload must be such that you can verify it even when the app
		 * wasn't the one who initiated the purchase flow (so that items
		 * purchased by the user on one device work on other devices owned by
		 * the user).
		 *
		 * Using your own server to store and verify developer payloads across
		 * app installations is recommended.
		 */

        return true;
    }

    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        //Log.d(TAG, "Destroying helper.");
        if (mHelper != null)
            mHelper.dispose();
        mHelper = null;
    }

    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo(Constantes.PACKAGE_FACEBOOK, 0); //Checks if FB is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse(Constantes.FACEBOOK_URI_PROFILE)); //Trys to make intent with FB's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse(Constantes.FACEBOOK_URL)); //catches and opens a url to the desired page
        }
    }
}
